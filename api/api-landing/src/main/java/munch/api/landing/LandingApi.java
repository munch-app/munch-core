package munch.api.landing;

import munch.api.ApiService;
import munch.restful.server.JsonCall;

import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 3:01 PM
 * Project: munch-core
 */
@Singleton
public final class LandingApi extends ApiService {

    @Override
    public void route() {
        PATH("/landing", () -> {
            POST("", this::post);
        });
    }

    public Map<String, Object> post(JsonCall call) {
        // Location
        // Queries
        // Collections
        //TODO
        return null;
    }
}
