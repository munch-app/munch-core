package munch.sitemap.permutation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.mashape.unirest.http.HttpMethod;
import com.typesafe.config.ConfigFactory;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.data.client.AreaClient;
import munch.data.client.LandmarkClient;
import munch.data.client.TagClient;
import munch.data.location.Area;
import munch.data.location.Landmark;
import munch.data.tag.Tag;
import munch.restful.client.RestfulRequest;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 10:17 AM
 * Project: munch-core
 */
public class PermutationService {
    protected final String apiUrl;

    protected final TagClient tagClient;
    protected final AreaClient areaClient;
    protected final LandmarkClient landmarkClient;

    @Inject
    public PermutationService(TagClient tagClient, AreaClient areaClient, LandmarkClient landmarkClient) {
        this.tagClient = tagClient;
        this.areaClient = areaClient;
        this.landmarkClient = landmarkClient;
        this.apiUrl = ConfigFactory.load().getString("api.url");
    }

    public Iterator<Set<Tag>> tagPermutations() {
        List<Tag> list = Lists.newArrayList(this::tagIterator);
        Set<Set<Tag>> tags = new HashSet<>();
        for (Tag tag1 : list) {
            for (Tag tag2 : list) {
                for (Tag tag3 : list) {
                    tags.add(Set.of(tag1, tag2, tag3));
                }
            }
        }
        return tags.iterator();
    }

    public Iterator<Tag> tagIterator() {
        return tagClient.iterator();
    }

    public Iterator<Area> areaIterator() {
        return areaClient.iterator();
    }

    public Iterator<Landmark> landmarkIterator() {
        return landmarkClient.iterator();
    }

    public List<JsonNode> search(SearchQuery searchQuery) {
        RestfulRequest request = new RestfulRequest(HttpMethod.POST, apiUrl + "/search");
        request.body(searchQuery);
        return request.asDataList(JsonNode.class);
    }

    public void put(NamedSearchQuery query) {
        // TODO: Dynamo DB Persist
    }
}
