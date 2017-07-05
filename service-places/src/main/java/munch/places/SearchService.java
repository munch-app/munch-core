package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.Place;
import munch.places.data.PlaceDatabase;
import munch.places.elastic.ElasticQuery;
import munch.restful.core.exception.ValidationException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 5:08 PM
 * Project: munch-core
 */
@Singleton
public final class SearchService implements JsonService {

    private final PlaceDatabase database;
    private final ElasticQuery search;

    /**
     * @param database place database service
     * @param search   place search service
     */
    @Inject
    public SearchService(PlaceDatabase database, ElasticQuery search) {
        this.database = database;
        this.search = search;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);
            POST("/suggest", this::suggest);
        });
    }

    /**
     * Note: Geometry follows elastic type hence follow:
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-shape.html
     * Geometry intersect function is applied
     * <pre>
     * {
     *     from: 0,
     *     size: 20,
     *     query: "", // Optional
     *     geometry: { // Optional
     *         "type": "multipolygon", // circle, polygon, multipolygon, geometrycollection are all supported
     *         "coordinates": [
     *              [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],
     *              [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],
     *              [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]
     *           ]
     *     },
     *     filters: { // Optional
     *
     *     }
     * }
     * </pre>
     * <p>
     * query: String = is for place name search
     * filter: Filters = apply filter for bounding search
     * geometry: GeoJson = apply spatial filter for bounding search
     * from: Int = start from
     * size: Int = size of query
     *
     * @param call json call
     * @return {data: list of place, total: size of all possible place}
     */
    private JsonNode search(JsonCall call) throws IOException {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        // Validate and search for error and fix
        validateFix(query);

        Pair<List<Place>, Integer> result = search.query(query);
        // Get data from database, remove the place if it is null
        List<String> ids = result.getLeft().stream().map(Place::getId).collect(Collectors.toList());
        List<Place> places = database.get(ids).stream().filter(Objects::nonNull).collect(Collectors.toList());

        // Return data: [] with total: Integer & linked: {} object
        return nodes(200, places).put("total", result.getRight());
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
        int size = ValidationException.require("size", request.path("size")).asInt();
        String query = ValidationException.requireNonBlank("query", request.path("query"));
        String latLng = request.path("latLng").asText(null);
        return search.suggest(query, latLng, size);
    }

    /**
     * Validate from, size
     * Validate points must be more than 3
     *
     * @param query query to validate and fix
     */
    private static void validateFix(SearchQuery query) {
        // From and Size not null validation
        ValidationException.requireNonNull("from", query.getFrom());
        ValidationException.requireNonNull("size", query.getSize());

        // Check if location contains polygon if exist
        if (query.getLocation() != null && query.getLocation().getPoints() != null) {
            if (query.getLocation().getPoints().size() < 3) {
                throw new ValidationException("location.points", "Points must have at least 3 points.");
            }
        }
    }
}
