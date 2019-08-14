package munch.api.location;

import com.fasterxml.jackson.databind.JsonNode;
import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2019-02-15
 * Time: 21:54
 * Project: munch-core
 */
@Singleton
public final class LocationService extends ApiService {

    private final LocationDatabase database;

    @Inject
    public LocationService(LocationDatabase database) {
        this.database = database;
    }

    @Override
    public void route() {
        PATH("/locations", () -> {
            POST("/current", this::current);
            POST("/search", this::search);
        });
    }

    public List<NamedLocation> search(JsonCall call, ApiRequest request) {
        JsonNode body = call.bodyAsJson();
        String text = body.path("text").asText();
        return database.search(text, request.getLatLng());
    }

    public NamedLocation current(JsonCall call) {
        JsonNode body = call.bodyAsJson();
        String latLng = body.path("latLng").asText();

        if (latLng == null) return null;
        return database.reverse(latLng);
    }
}
