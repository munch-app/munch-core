package munch.api.place;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 5/11/18
 * Time: 1:43 PM
 * Project: munch-core
 */
@Singleton
public final class SuggestPlaceService extends ApiService {

    @Override
    public void route() {
        PATH("/suggests/places", () -> {
            POST("", this::suggest);
        });
    }

    public JsonResult suggest(JsonCall call) {
        ApiRequest request = call.get(ApiRequest.class);

        String userId = request.getUserId();
        JsonNode payload = call.bodyAsJson();
        return JsonResult.ok(payload);
    }
}
