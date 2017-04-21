package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:50 PM
 * Project: munch-core
 */
@Singleton
public class MenuService implements JsonService {

    @Override
    public void route() {
        PATH("/places/menu", () -> {
            GET("/:id", this::put);
            PUT("/:id", this::put);
            DELETE("/:id", this::put);
        });
    }

    private JsonNode put(JsonCall call) {
        return null;
    }

}
