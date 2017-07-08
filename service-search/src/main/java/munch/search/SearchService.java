package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.core.exception.ValidationException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.search.data.Location;
import munch.search.data.SearchQuery;
import munch.search.elastic.ElasticClient;
import munch.search.elastic.PlaceBoolQuery;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 6/7/2017
 * Time: 6:36 AM
 * Project: munch-core
 */
@Singleton
public class SearchService implements JsonService {

    private final ElasticClient client;
    private final PlaceBoolQuery placeBoolQuery;
    private final Location.Marshaller locationMarshaller;

    @Inject
    public SearchService(ElasticClient client, PlaceBoolQuery placeBoolQuery, Location.Marshaller locationMarshaller) {
        this.client = client;
        this.placeBoolQuery = placeBoolQuery;
        this.locationMarshaller = locationMarshaller;
    }

    @Override
    public void route() {
        POST("/search", this::search);
        POST("/suggest", this::suggest);
    }

    /**
     * Currently search only support place data
     * <p>
     * query: String = is for place name search
     * filter: Filters = apply filter for bounding search
     * from: Int = start from
     * size: Int = size of query
     *
     * @param call json call
     * @return { "data": [places], "total": size}
     */
    private JsonNode search(JsonCall call) throws IOException {
        // Validate and search for error and fixes it
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        SearchQuery.validateFix(query);

        JsonNode boolQuery = placeBoolQuery.make(query);
        JsonNode result = client.postBoolSearch("place", query.getFrom(), query.getSize(), boolQuery);
        JsonNode hits = result.path("hits");

        // Return data: [] with total: Integer & linked: {} object
        ObjectNode nodes = nodes(200, parse(hits.path("hits")));
        nodes.put("total", hits.path("total").asInt());
        return nodes;
    }

    /**
     * Note: Geometry follows elastic type hence follow:
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-shape.html
     * Geometry intersect function is applied
     * <pre>
     * {
     *     size: 20,
     *     query: "", // Mandatory
     *     latLng: "lat,lng" // Optional
     * }
     * </pre>
     * <p>
     * query: String = is for place name search
     * latLng: String = to provide radius context to the suggestion
     * size: Int = size of query
     *
     * @param call    json call
     * @param request json body
     * @return {data: list of place, total: size of all possible place}
     */
    private JsonNode suggest(JsonCall call, JsonNode request) throws IOException {
        int size = ValidationException.require("size", request.path("size")).asInt();
        String query = ValidationException.requireNonBlank("query", request.path("query"));
        String latLng = request.path("latLng").asText(null);
        JsonNode results = client.suggest(null, query, latLng, size);
        return nodes(200, parse(results));
    }

    /**
     * @param results result from elastic search
     * @return result that is compatible for munch-core
     */
    private ArrayNode parse(JsonNode results) {
        // TODO Parse Place & Location Result
        return null;
    }
}
