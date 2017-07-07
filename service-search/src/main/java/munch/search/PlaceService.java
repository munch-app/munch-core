package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.search.data.Place;
import munch.search.data.SearchQuery;
import munch.search.elastic.ElasticClient;
import munch.search.elastic.ElasticDatabase;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/7/2017
 * Time: 6:36 AM
 * Project: munch-core
 */
@Singleton
public class PlaceService implements JsonService {

    private final ElasticDatabase index;
    private final ElasticClient search;

    @Inject
    public PlaceService(ElasticDatabase index, ElasticClient search) {
        this.index = index;
        this.search = search;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);
            PUT("/:id", this::put);

            DELETE("/before/:updatedDate", this::deleteBefore);
            DELETE("/:id", this::delete);
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
        // Validate and search for error and fixes it
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        SearchQuery.validateFix(query);

        Pair<List<Place>, Integer> result = search.query(query);

        // Return data: [] with total: Integer & linked: {} object
        ObjectNode nodes = nodes(200, result.getLeft());
        nodes.put("total", result.getRight());
        return nodes;
    }

    /**
     * @param call json call
     * @return 200 = saved
     */
    private JsonNode put(JsonCall call) throws Exception {
        Place place = call.bodyAsObject(Place.class);
        index.put(place);
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = deleted
     */
    private JsonNode delete(JsonCall call) throws Exception {
        String id = call.pathString("id");
        index.delete("place", id);
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = deleted
     */
    private JsonNode deleteBefore(JsonCall call) throws Exception {
        long updated = call.pathLong("updatedDate");
        index.deleteBefore("place", updated);
        return Meta200;
    }
}
