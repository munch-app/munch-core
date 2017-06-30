package munch.api.services;

import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.RouteGroup;
import spark.Spark;

import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class AbstractService implements JsonService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractService.class);

    private static final String Version = "/v1";

    protected static final String HEADER_LOCATION_LATLNG = "Location-LatLng";
    protected static final String HEADER_LOCATION_CITY = "Location-City";

    @Override
    public void PATH(String path, RouteGroup routeGroup) {
        Spark.path(Version + path, routeGroup);
    }

    /**
     * @param call json call to find the header
     * @return Optional latLng
     */
    public Optional<LatLng> getHeaderLatLng(JsonCall call) {
        String latLng = call.getHeader(HEADER_LOCATION_LATLNG);
        if (latLng == null) return Optional.empty();

        try {
            return Optional.of(new LatLng(latLng));
        } catch (NullPointerException | IndexOutOfBoundsException | NumberFormatException e) {
            logger.warn("Unable to parse Header: Location-LatLng value: {}", latLng, e);
            return Optional.empty();
        }
    }

    /**
     * Special wrapper for HeaderL Location-LatLng
     */
    public static class LatLng {
        private final double lat;
        private final double lng;

        /**
         * @param latLng latLng is string separated by ,
         * @throws NumberFormatException     if number is not double
         * @throws IndexOutOfBoundsException if size is not 2
         * @throws NullPointerException      latlng is null, or either split string is null
         */
        public LatLng(String latLng) throws NullPointerException, IndexOutOfBoundsException, NumberFormatException {
            String[] split = latLng.split(",");
            lat = Double.parseDouble(split[0].trim());
            lng = Double.parseDouble(split[1].trim());
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}
