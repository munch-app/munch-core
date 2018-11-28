package munch.permutation;

import com.mashape.unirest.http.HttpMethod;
import com.typesafe.config.ConfigFactory;
import munch.api.search.SearchQuery;
import munch.api.search.filter.FilterResult;
import munch.data.location.Area;
import munch.data.tag.Tag;
import munch.restful.client.RestfulRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 31/10/18
 * Time: 1:28 AM
 * Project: munch-core
 */
@Singleton
public final class APISearchClient {
    protected final String apiUrl;

    @Inject
    public APISearchClient() {
        this.apiUrl = ConfigFactory.load().getString("api.url");
    }

    public long count(List<Area> areas, List<Tag> tags) {
        SearchQuery query = new SearchQuery();
        query.getFilter().setTags(tags);

        SearchQuery.Filter.Location location = new SearchQuery.Filter.Location();
        location.setType(SearchQuery.Filter.Location.Type.Where);
        location.setAreas(areas);

        query.getFilter().setLocation(location);
        return count(query);
    }

    /**
     * Even through this method is not efficient but it is the most modularised
     *
     * @param searchQuery with
     * @return List of Card results
     */
    public long count(SearchQuery searchQuery) {
        RestfulRequest request = new RestfulRequest(HttpMethod.POST, apiUrl + "/search/filter");
        request.body(searchQuery);
        FilterResult result = request.asDataObject(FilterResult.class);
        return result.getCount();
    }
}
