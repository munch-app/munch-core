package munch.api.user;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserPlaceActivityClient;
import munch.user.data.UserPlaceActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 8/6/18
 * Time: 2:54 PM
 * Project: munch-core
 */
@Singleton
public final class UserPlaceActivityService extends ApiService {

    private final UserPlaceActivityClient activityClient;

    @Inject
    public UserPlaceActivityService(UserPlaceActivityClient activityClient) {
        this.activityClient = activityClient;
    }

    @Override
    public void route() {
        PATH("/users/places/activities", () -> {
            PUT("/:placeId/:startedMillis", this::put);
        });
    }

    private JsonResult put(JsonCall call) {
        String userId = call.get(ApiRequest.class).getUserId();
        String placeId = call.pathString("placeId");
        long startedMillis = call.pathLong("startedMillis");

        UserPlaceActivity placeActivity = call.bodyAsObject(UserPlaceActivity.class);
        activityClient.put(userId, placeId, startedMillis, placeActivity);
        return JsonResult.ok();
    }
}
