package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import munch.restful.server.JsonService;
import munch.restful.server.exceptions.JsonException;
import munch.restful.server.exceptions.ParamException;
import spark.RouteGroup;
import spark.Spark;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class AbstractEndpoint implements JsonService {

    private static final String Version = "/v1";

    @Override
    public void path(String path, RouteGroup routeGroup) {
        Spark.path(Version + path, routeGroup);
    }

    /**
     * @param rootNode root node with {spatial:{}}
     * @return Spatial object
     * @throws ParamException if spatial: {} not found
     */
    protected static Spatial getSpatial(JsonNode rootNode) {
        return new Spatial(rootNode);
    }

    /**
     * Spatial node
     * Spatial node is used to understand where to user is at
     * <pre>
     * {
     *      spatial:{
     *          lat: 1.34
     *          lng: 103.8
     *      }
     *  }
     * </pre>
     */
    public static class Spatial {

        private final double lat;
        private final double lng;

        /**
         * @param rootNode root node to extract spatial data
         */
        public Spatial(JsonNode rootNode) {
            JsonNode spatial = rootNode.path("spatial");
            if (!(spatial.has("lat") && spatial.has("lng"))) {
                throw new JsonException("Required spatial node not found in root node.");
            }
            this.lat = spatial.path("lat").asDouble();
            this.lng = spatial.path("lng").asDouble();
        }

        /**
         * @return current latitude of user
         */
        public double getLat() {
            return lat;
        }

        /**
         * @return current longitude of user
         */
        public double getLng() {
            return lng;
        }
    }
}
