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
public class SearchService implements JsonService {

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("/place", this::searchPlace);
        });
    }

    private JsonNode searchPlace(JsonCall call) {
        // TODO
        return null;
    }
}
