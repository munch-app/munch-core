package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.services.locations.LocationSelector;
import munch.data.Location;
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

    private final SearchClient searchClient;
    private final LocationSelector locationSelector;

    @Inject
    public LocationService(SearchClient searchClient, LocationSelector locationSelector) {
        this.searchClient = searchClient;
        this.locationSelector = locationSelector;
    }

    /**
     * Endpoint: /v/neighborhood/*
     */
    @Override
    public void route() {
        PATH("/locations", () -> {
            // Find Location to use for Discovery Service
            GET("/find", this::find);
            // Reverse Location from where the user is standing,
            // normally used when the user clicked on current location
            GET("/reverse", this::reverse);
            // Suggest Location when user is searching with location service
            GET("/suggest", this::suggest);
        });
    }

    /**
     * @param call json call
     * @return generated current Location of user
     */
    private Location find(JsonCall call) {
        String latLng = call.queryString("latLng");
        return locationSelector.select(latLng);
    }

    /**
     * ?latLng=lat,lng
     * To be used by the Location search when user click current location
     *
     * @param call json call
     * @return Location or NULL
     * code: 200 = ok
     */
    private Location reverse(JsonCall call) {
        String latLng = call.queryString("latLng");
        return searchClient.reverseLocation(latLng);
    }

    /**
     * ?text={String}
     *
     * @param call json call
     * @return List of Location or empty
     * code: 200 = ok
     */
    private List<Location> suggest(JsonCall call) {
        String text = call.queryString("text");
        return searchClient.suggestLocation(text, 20);
    }
}
