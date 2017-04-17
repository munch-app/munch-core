package munch.api.endpoints;

import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.commons.lang3.StringUtils;
import spark.RouteGroup;
import spark.Spark;

import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class AbstractEndpoint implements JsonService {

    private static final String Version = "/v1";

    @Override
    public void PATH(String path, RouteGroup routeGroup) {
        Spark.path(Version + path, routeGroup);
    }

    /**
     * Required Headers:
     * Spatial-Lat
     * Spatial-Lng
     *
     * @param call json call to search header object
     * @return Optional Spatial object
     */
    protected static Optional<Spatial> getSpatial(JsonCall call) {
        String lat = call.getHeader("Spatial-Lat");
        String lng = call.getHeader("Spatial-Lng");
        if (StringUtils.isAnyBlank(lat, lng)) return Optional.empty();
        return Optional.of(new Spatial(lat, lng));
    }

    /**
     * Spatial header
     */
    public static class Spatial {
        private final double lat;
        private final double lng;

        protected Spatial(String lat, String lng) {
            this.lat = Double.parseDouble(lat);
            this.lng = Double.parseDouble(lng);
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
