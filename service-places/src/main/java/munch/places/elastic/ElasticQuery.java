package munch.places.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import munch.places.SearchQuery;
import munch.places.data.Place;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public final class ElasticQuery {
    private final RestClient client;
    private final ObjectMapper mapper;

    private final ArrayNode searchSources;
    private final ArrayNode suggestSources;
    private final BoolQuery boolQuery;

    @Inject
    public ElasticQuery(RestClient client, ObjectMapper mapper, BoolQuery boolQuery) {
        this.client = client;
        this.mapper = mapper;
        this.boolQuery = boolQuery;

        // Sources of data to return
        this.searchSources = mapper.createArrayNode();
        this.suggestSources = mapper.createArrayNode()
                .add("name")
                .add("location.latLng")
                .add("location.address")
                .add("location.city");
    }

    /**
     * @param query search query object
     * @return pair = (list of Place, total results)
     * @throws IOException exception
     * @see SearchQuery.Filters
     */
    public Pair<List<Place>, Integer> query(SearchQuery query) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("from", query.getFrom());
        root.put("size", query.getSize());
        root.set("_source", searchSources);
        root.set("query", mapper.createObjectNode().set("bool", boolQuery.make(query)));

        // Query, parse and return
        JsonNode hits = postSearch(root).path("hits");
        List<Place> places = new ArrayList<>();
        for (JsonNode hit : hits.path("hits")) places.add(parse(hit));
        return Pair.of(places, hits.path("total").asInt());
    }

    /**
     * @param query  query string
     * @param latLng nullable latLng
     * @param size   size of suggestion of place
     * @return List of suggested place
     */
    public List<Place> suggest(String query, @Nullable String latLng, int size) throws IOException {
        ObjectNode completion = mapper.createObjectNode();
        completion.put("field", "suggest");
        completion.put("size", size);
        if (StringUtils.isNotBlank(latLng)) {
            ObjectNode contexts = mapper.createObjectNode();
            ObjectNode location = mapper.createObjectNode();

            String[] lls = latLng.split(",");
            location.put("lat", Double.parseDouble(lls[0].trim()));
            location.put("lon", Double.parseDouble(lls[1].trim()));
            contexts.set("latLng", location);
            completion.set("contexts", contexts);
        }

        ObjectNode placeSuggest = mapper.createObjectNode();
        placeSuggest.put("prefix", query);
        placeSuggest.set("completion", completion);

        ObjectNode suggest = mapper.createObjectNode();
        suggest.set("place-suggest", placeSuggest);
        ObjectNode root = mapper.createObjectNode();
        root.set("suggest", suggest);
        root.set("_source", suggestSources);

        // Query, parse and return
        List<Place> places = new ArrayList<>();
        JsonNode result = postSearch(root).path("suggest").path("place-suggest");
        for (JsonNode each : result.get(0).path("options")) {
            places.add(parse(each.path("options")));
        }
        return places;
    }

    /**
     * @param node search node
     * @return root node
     */
    private JsonNode postSearch(JsonNode node) throws IOException {
        HttpEntity jsonEntity = new NStringEntity(mapper.writeValueAsString(node), ContentType.APPLICATION_JSON);
        Response response = client.performRequest("POST", "/munch/place/_search", Collections.emptyMap(), jsonEntity);
        HttpEntity entity = response.getEntity();

        try {
            InputStream input = entity.getContent();
            return mapper.readTree(input);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    /**
     * @param node json node
     * @return parsed place
     */
    private static Place parse(JsonNode node) {
        Place place = new Place();
        place.setId(node.path("_id").asText());
        JsonNode source = node.path("_source");
        place.setName(source.path("name").asText());

        if (source.has("location")) {
            JsonNode location = source.path("location");
            place.setLocation(new Place.Location());
            place.getLocation().setAddress(location.path("address").asText(null));
            place.getLocation().setCity(location.path("city").asText(null));
            place.getLocation().setLatLng(location.path("latLng").asText(null));
        }
        return place;
    }
}
