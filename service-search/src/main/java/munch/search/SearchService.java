package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.core.exception.ValidationException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.search.data.Place;
import munch.search.elastic.ElasticClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/7/2017
 * Time: 6:36 AM
 * Project: munch-core
 */
@Singleton
public class SearchService implements JsonService {

    private final ElasticClient search;

    @Inject
    public SearchService(ElasticClient search) {
        this.search = search;
    }

    @Override
    public void route() {
        POST("/suggest", this::suggest);
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
    private List<Place> suggest(JsonCall call, JsonNode request) throws IOException {
        // TODO for Suggestions for variable types
        int size = ValidationException.require("size", request.path("size")).asInt();
        String query = ValidationException.requireNonBlank("query", request.path("query"));
        String latLng = request.path("latLng").asText(null);
        return search.suggest(query, latLng, size);
    }
}
