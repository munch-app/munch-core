package munch.search.place;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.struct.place.Place;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 5:08 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceService implements JsonService {

    private final PlaceIndex index;
    private final PlaceSearch search;

    @Inject
    public PlaceService(PlaceIndex index, PlaceSearch search) {
        this.index = index;
        this.search = search;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);

            // Batch index operations
            new Batch().route();
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
     *     geometry: { // Optional
     *         "type": "multipolygon", // circle, polygon, multipolygon, geometrycollection are all supported
     *         "coordinates": [
     *              [[[102.0, 2.0], [103.0, 2.0], [103.0, 3.0], [102.0, 3.0], [102.0, 2.0]]],
     *              [[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]],
     *              [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]
     *           ]
     *     }
     * }
     * </pre>
     *
     * @param call    json call
     * @param request body node
     * @return {data: list of place, total: size of all possible place}
     */
    private JsonNode search(JsonCall call, JsonNode request) throws IOException {
        int from = request.path("from").asInt();
        int size = request.path("size").asInt();

        // Check geometry node
        JsonNode geometry = null;
        if (request.has("geometry")) {
            geometry = request.path("geometry");
        }

        Pair<List<Place>, Integer> result = search.query(from, size, geometry, null);
        ObjectNode node = nodes(200, result.getLeft());
        node.put("total", result.getRight());
        return node;
    }

    /**
     * Batch operations of place
     */
    private class Batch implements JsonService {

        @Override
        public void route() {
            PATH("/batch", () -> {
                POST("/put", this::put);
                POST("/delete", this::delete);
            });
        }

        /**
         * @param call json call
         * @return 200 = saved
         */
        private JsonNode put(JsonCall call) throws Exception {
            List<Place> places = call.bodyAsList(Place.class);
            index.put(places);
            return Meta200;
        }

        /**
         * @param call json call
         * @return 200 = deleted
         */
        private JsonNode delete(JsonCall call) throws Exception {
            List<String> keys = call.bodyAsList(String.class);
            index.delete(keys);
            return Meta200;
        }
    }
}
