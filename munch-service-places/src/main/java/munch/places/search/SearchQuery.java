package munch.places.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import munch.struct.places.Place;
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
public class SearchQuery extends SearchFilter {

    private final RestClient client;
    private final ObjectMapper mapper;

    private final ArrayNode sources;

    @Inject
    public SearchQuery(RestClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;

        // Sources of data to return
        this.sources = mapper.createArrayNode();
        sources.add("name");
    }

    /**
     * @param from     start from: pagination
     * @param size     size for: pagination
     * @param geometry geometry for within function
     * @param query    text query string TODO not working yet
     *                 // TODO param filters in future
     * @return list of Place, total results
     * @throws IOException exception
     */
    public Pair<List<Place>, Integer> query(int from, int size, @Nullable JsonNode geometry,
                                            @Nullable String query, @Nullable Filters filters) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("from", from);
        root.put("size", size);
        root.set("_source", sources);

        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", mapper.createObjectNode().set("match_all", mapper.createObjectNode()));

        if (query != null) {
            // TODO name search first
        }

        // Create geometry filter if is not null or missing
        if (geometry != null && !geometry.isNull() && !geometry.isMissingNode()) {
            bool.set("filter", createGeometryFilter(geometry));
        }

        if (filters != null) {
            // TODO filter search
        }

        root.set("query", mapper.createObjectNode().set("bool", bool));
        return query(root);
    }

    private Pair<List<Place>, Integer> query(JsonNode node) throws IOException {
        JsonNode hits = search(node).path("hits");

        // Create places list
        List<Place> places = new ArrayList<>();
        for (JsonNode hit : hits.path("hits")) places.add(parse(hit));

        return Pair.of(places, hits.path("total").asInt());
    }

    /**
     * @param node search node
     * @return root node
     */
    private JsonNode search(JsonNode node) throws IOException {
        HttpEntity jsonEntity = new NStringEntity(mapper.writeValueAsString(node), ContentType.APPLICATION_JSON);
        Response response = client.performRequest("POST", "/munch/place/_search", Collections.emptyMap(), jsonEntity);

        HttpEntity entity = response.getEntity();
        InputStream input = entity.getContent();
        try {
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
        place.setName(node.path("_source").path("name").asText());
        return place;
    }
}
