package munch.api.user;

import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSearchPreferenceClient;
import munch.user.data.UserSearchPreference;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 9/12/18
 * Time: 11:14 PM
 * Project: munch-core
 */
@Singleton
public final class UserSearchPreferenceService extends ApiService {

    private final UserSearchPreferenceClient client;

    @Inject
    public UserSearchPreferenceService(UserSearchPreferenceClient client) {
        this.client = client;
    }

    @Override
    public void route() {
        PATH("/users/search/preference", () -> {
            GET("", this::get);
            PUT("", this::put);
        });
    }

    public UserSearchPreference get(JsonCall call) {
        String userId = call.get(ApiRequest.class).getAccountId();
        return client.get(userId);
    }

    public JsonResult put(JsonCall call) {
        String userId = call.get(ApiRequest.class).getAccountId();
        UserSearchPreference preference = call.bodyAsObject(UserSearchPreference.class);

        client.put(userId, preference);
        return JsonResult.ok();
    }
}
