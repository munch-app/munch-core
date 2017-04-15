package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.endpoints.service.GeocoderClient;
import munch.restful.server.JsonCall;

/**
 * Created by: Fuxing
 * Date: 13/4/2017
 * Time: 1:48 AM
 * Project: munch-core
 */
@Singleton
public class LocationEndpoint extends AbstractEndpoint {

    private final GeocoderClient geocoder;

    @Inject
    public LocationEndpoint(GeocoderClient geocoder) {
        this.geocoder = geocoder;
    }

    @Override
    public void route() {
        path("/locations", () -> {
            get("/search", this::search);
            get("/geocode", this::geocode);
            get("/reverse", this::reverse);
        });
    }

    private JsonNode reverse(JsonCall call) {
        double lat = call.queryDouble("lat");
        double lng = call.queryDouble("lng");
        // TODO
        return null;
    }

    private JsonNode search(JsonCall call) {
        String text = call.queryString("text");
        // TODO
        return null;
    }

    private JsonNode geocode(JsonCall call) {
        String text = call.queryString("text");
        // TODO
        return null;
    }
}
