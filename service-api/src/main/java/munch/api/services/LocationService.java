package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.data.Location;
import munch.restful.server.JsonCall;

import javax.annotation.Nullable;
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

    @Inject
    public LocationService(SearchClient searchClient) {
        this.searchClient = searchClient;
    }

    /**
     * Endpoint: /v/neighborhood/*
     */
    @Override
    public void route() {
        PATH("/locations", () -> {
            // Reverse Location from where the user is standing,
            // normally used when the user clicked on current location
            GET("/reverse", this::reverse);
            // Suggest Location when user is searching with location service
            GET("/suggest", this::suggest);
        });
    }

    /**
     * ?latLng=lat,lng
     * To be used by the Location search when user click current location
     *
     * @param call json call
     * @return Location or NULL
     * code: 200 = ok, 404 = not found
     */
    @Nullable
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
