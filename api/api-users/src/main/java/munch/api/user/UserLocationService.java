package munch.api.user;

import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.user.client.UserLocationClient;
import munch.user.data.UserLocation;
import munch.user.data.UserLocationIndex;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-12
 * Time: 00:59
 * Project: munch-core
 */
@Singleton
public final class UserLocationService extends ApiService {

    private final UserLocationClient locationClient;

    @Inject
    public UserLocationService(UserLocationClient locationClient) {
        this.locationClient = locationClient;
    }

    @Override
    public void route() {
        PATH("/users/locations", () -> {
            GET("", this::list);
            POST("", this::post);
            DELETE("/:sortId", this::delete);
        });
    }

    public NextNodeList<UserLocation> list(JsonCall call, ApiRequest request) {
        final String userId = request.getAccountId();
        final int size = call.querySize(30, 50);
        final UserLocationIndex index = UserLocationIndex.sortId;

        String next = call.queryNext(index.getRangeName(), String.class);
        return locationClient.list(index, userId, next, size);
    }

    public UserLocation post(JsonCall call, ApiRequest request) {
        final String userId = request.getAccountId();
        UserLocation location = call.bodyAsObject(UserLocation.class);
        location.setUserId(userId);
        return locationClient.post(location);
    }

    public UserLocation delete(JsonCall call, ApiRequest request) {
        final String userId = request.getAccountId();
        final String sortId = call.pathString("sortId");

        return locationClient.delete(userId, sortId);
    }
}
