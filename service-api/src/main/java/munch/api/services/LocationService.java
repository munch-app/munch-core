package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.clients.StaticJsonResource;
import munch.data.Location;
import munch.restful.server.JsonCall;

import javax.annotation.Nullable;
import java.io.IOException;
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
    private final StaticJsonResource jsonResource;

    @Inject
    public LocationService(SearchClient searchClient, StaticJsonResource jsonResource) {
        this.searchClient = searchClient;
        this.jsonResource = jsonResource;
    }

    /**
     * Endpoint: /v/neighborhood/*
     */
    @Override
    public void route() {
        PATH("/locations", () -> {
            GET("/popular", this::popular);
            HEAD("/popular", this::popularHead);

            // Reverse Location from where the user is standing,
            // normally used when the user clicked on current location
            GET("/reverse", this::reverse);
            // Suggest Location when user is searching with location service
            GET("/suggest", this::suggest);
        });
    }

    private JsonNode popular(JsonCall call) throws IOException {
        call.response().header("Last-Modified", "1505473814000");
        return jsonResource.getResource("popular-locations.json");
    }

    private JsonNode popularHead(JsonCall call) {
        call.response().header("Last-Modified", "1505473814000");
        return nodes(200);
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
