package munch.api.endpoints;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.GeocoderClient;
import munch.api.struct.Neighborhood;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 13/4/2017
 * Time: 1:48 AM
 * Project: munch-core
 */
@Singleton
public class NeighborhoodEndpoint extends AbstractEndpoint {

    private final GeocoderClient geocoder;

    @Inject
    public NeighborhoodEndpoint(GeocoderClient geocoder) {
        this.geocoder = geocoder;
    }

    @Override
    public void route() {
        PATH("/neighborhood", () -> {
            GET("/search", this::search);
            GET("/geocode", this::geocode);
            GET("/reverse", this::reverse);
        });
    }

    private Neighborhood reverse(JsonCall call) {
        double lat = call.queryDouble("lat");
        double lng = call.queryDouble("lng");
        return geocoder.reverse(lat, lng);
    }

    private List<Neighborhood> search(JsonCall call) {
        String text = call.queryString("text");
        return geocoder.search(text);
    }

    private Neighborhood geocode(JsonCall call) {
        String text = call.queryString("text");
        return geocoder.geocode(text);
    }
}
