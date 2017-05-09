package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.Place;
import munch.places.data.PlaceDatabase;
import munch.places.search.Filters;
import munch.places.search.SearchQuery;
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
    private final SearchQuery search;

    /**
     * @param database place database service
     * @param search   place search service
     */
    @Inject
    public SearchService(PlaceDatabase database, SearchQuery search) {
        this.database = database;
        this.search = search;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);
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
     * @param call    json call
     * @param request body node
     * @return {data: list of place, total: size of all possible place}
     */
    private JsonNode search(JsonCall call, JsonNode request) throws IOException {
        int from = request.path("from").asInt();
        int size = request.path("size").asInt();
        String query = request.path("query").asText(null);
        JsonNode geometry = request.get("geometry");
        Filters filters = request.has("filters") ?
                readObject(request.get("filters"), Filters.class) : null;

        Pair<List<Place>, Integer> result = search.query(from, size, geometry, query, filters);
        // Get data from database, remove the place if it is null
        List<Place> places = database.get(result.getLeft().stream().map(Place::getId).collect(Collectors.toList()));
        places.removeIf(Objects::isNull);

        // Return data: [] with total: Integer & linked: {} object
        return nodes(200, places).put("total", result.getRight());
    }
}
