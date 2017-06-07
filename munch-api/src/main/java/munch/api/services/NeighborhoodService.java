package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.GeocoderClient;
import munch.api.data.Neighborhood;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 13/4/2017
 * Time: 1:48 AM
 * Project: munch-core
 */
@Singleton
public class NeighborhoodService extends AbstractService {

    private final GeocoderClient geocoder;

    @Inject
    public NeighborhoodService(GeocoderClient geocoder) {
        this.geocoder = geocoder;
    }

    /**
     * Endpoint: /v/neighborhood/*
     */
    @Override
    public void route() {
        PATH("/neighborhood", () -> {
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
    private Neighborhood reverse(JsonCall call) {
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
    private List<Neighborhood> search(JsonCall call) {
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
    private Neighborhood geocode(JsonCall call) {
        String text = call.queryString("text");
        return geocoder.geocode(text);
    }
}
