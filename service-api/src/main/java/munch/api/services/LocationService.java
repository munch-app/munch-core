package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.StaticJsonClient;
import munch.data.clients.LocationClient;
import munch.data.structure.Location;
import munch.restful.server.JsonCall;

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

    private final LocationClient locationClient;
    private final StaticJsonClient jsonResource;

    @Inject
    public LocationService(LocationClient locationClient, StaticJsonClient jsonResource) {
        this.locationClient = locationClient;
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
     * ?text={String}
     *
     * @param call json call
     * @return List of Location or empty
     * code: 200 = ok
     */
    private List<Location> suggest(JsonCall call) {
        String text = call.queryString("text");
        return locationClient.suggest(text, 10);
    }
}
