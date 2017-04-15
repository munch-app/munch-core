package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 16/4/2017
 * Time: 3:57 AM
 * Project: munch-core
 */
@Singleton
public class IndexService implements JsonService {

    @Override
    public void route() {
        path("/index", () -> {
            put("/places", this::putPlaces);
            delete("/places", this::deletePlaces);
        });
    }

    private JsonNode putPlaces(JsonCall call, JsonNode node) {
        // TODO
        return null;
    }

    private JsonNode deletePlaces(JsonCall call, JsonNode node) {
        return null;
    }
}
