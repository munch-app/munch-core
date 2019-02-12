package munch.api.user;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.server.JsonCall;
import munch.user.client.UserRatedPlaceClient;
import munch.user.data.UserRatedPlace;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-12
 * Time: 00:58
 * Project: munch-core
 */
@Singleton
public final class UserRatedPlaceService extends ApiService {

    private final UserRatedPlaceClient ratedPlaceClient;

    @Inject
    public UserRatedPlaceService(UserRatedPlaceClient ratedPlaceClient) {
        this.ratedPlaceClient = ratedPlaceClient;
    }

    @Override
    public void route() {
        PATH("/users/rated/places", () -> {
            PUT("/:placeId", this::put);
            PATCH("/:placeId", this::patch);
        });
    }

    public UserRatedPlace put(JsonCall call, ApiRequest request) {
        final String userId = request.getUserId();
        UserRatedPlace ratedPlace = call.bodyAsObject(UserRatedPlace.class);
        ratedPlace.setUserId(userId);

        ratedPlaceClient.put(ratedPlace);
        return ratedPlace;
    }

    public UserRatedPlace patch(JsonCall call, ApiRequest request) {
        final String userId = request.getUserId();
        final String placeId = call.pathString("placeId");

        return ratedPlaceClient.patch(userId, placeId, call.bodyAsJson());
    }
}
