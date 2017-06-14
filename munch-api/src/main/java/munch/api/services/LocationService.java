package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.LocationClient;
import munch.api.data.Location;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 13/4/2017
 * Time: 1:48 AM
 * Project: munch-core
 */
@Singleton
public class LocationService extends AbstractService {

    private final LocationClient geocoder;

    @Inject
    public LocationService(LocationClient geocoder) {
        this.geocoder = geocoder;
    }

    /**
     * Endpoint: /v/neighborhood/*
     */
    @Override
    public void route() {
        PATH("/location", () -> {
            GET("/search", this::search);
            GET("/geocode", this::geocode);
            GET("/reverse", this::reverse);
        });
    }

    /**
     * ?lat={Double}&lng={Double}
     *
     * @param call json call
     * @return Neighborhood or NULL
     * code: 200 = ok
     */
    private Location reverse(JsonCall call) {
        double lat = call.queryDouble("lat");
        double lng = call.queryDouble("lng");
        return geocoder.reverse(lat, lng);
    }

    /**
     * ?text={String}
     *
     * @param call json call
     * @return List of Neighborhood or empty
     * code: 200 = ok
     */
    private List<Location> search(JsonCall call) {
        String text = call.queryString("text");
        return geocoder.search(text);
    }

    /**
     * ?text={String}
     *
     * @param call json call
     * @return Neighborhood or NULL
     * code: 200 = ok
     */
    private Location geocode(JsonCall call) {
        String text = call.queryString("text");
        return geocoder.geocode(text);
    }
}
