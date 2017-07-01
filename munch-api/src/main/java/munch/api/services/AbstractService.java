package munch.api.services;

import munch.api.data.LatLng;
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
}
