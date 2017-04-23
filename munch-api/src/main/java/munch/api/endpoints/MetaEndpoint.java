package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 8:11 AM
 * Project: munch-core
 */
@Singleton
public class MetaEndpoint extends AbstractEndpoint {

    @Override
    public void route() {
        PATH("/meta", () -> {
            GET("/version", this::version);
        });
    }

    public JsonNode version(JsonCall call) {
        return null;
    }
}
