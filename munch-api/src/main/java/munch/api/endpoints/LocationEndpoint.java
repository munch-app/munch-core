package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import munch.restful.server.JsonCall;

/**
 * Created by: Fuxing
 * Date: 13/4/2017
 * Time: 1:48 AM
 * Project: munch-core
 */
public class LocationEndpoint extends AbstractEndpoint {

    @Override
    public void route() {
        path("/locations", () -> {
            // Search might require spatial data in the future
            post("/search", this::search);
            get("/geocode", this::geocode);
        });
    }

    private JsonNode search(JsonCall call) {
        return null;
    }

    private JsonNode geocode(JsonCall call) {
        String[] latLng = call.queryString("latlng").split(",");

        return null;
    }
}
