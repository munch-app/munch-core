package munch.api.landing;

import munch.api.ApiRequest;
import munch.restful.server.JsonCall;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 3:52 PM
 * Project: munch-core
 */
public final class LandingRequest {
    private final JsonCall call;
    private final ApiRequest apiRequest;

    LandingRequest(JsonCall call) {
        this.call = call;
        this.apiRequest = call.get(ApiRequest.class);
    }

    public JsonCall getCall() {
        return call;
    }

    public ApiRequest getApiRequest() {
        return apiRequest;
    }
}
