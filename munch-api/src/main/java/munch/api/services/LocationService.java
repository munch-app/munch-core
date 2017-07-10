package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
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
            GET("/reverse", this::reverse);
            GET("/suggest", this::suggest);
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
        String latLng = call.queryString("latLng");
        return searchClient.reverseLocation(latLng);
    }

    /**
     * ?text={String}
     *
     * @param call json call
     * @return List of Neighborhood or empty
     * code: 200 = ok
     */
    private List<Location> suggest(JsonCall call) {
        String text = call.queryString("text");
        return searchClient.suggestLocation(text, 20);
    }
}
