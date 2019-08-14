package munch.api.user;

import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.server.JsonCall;
import munch.taste.PlaceRatingClient;
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
    private final PlaceRatingClient placeRatingClient;

    @Inject
    public UserRatedPlaceService(UserRatedPlaceClient ratedPlaceClient, PlaceRatingClient placeRatingClient) {
        this.ratedPlaceClient = ratedPlaceClient;
        this.placeRatingClient = placeRatingClient;
    }

    @Override
    public void route() {
        PATH("/users/rated/places", () -> {
            PUT("/:placeId", this::put);
        });
    }

    public UserRatedPlace put(JsonCall call, ApiRequest request) {
        final String userId = request.getAccountId();
        final String placeId = call.pathString("placeId");

        UserRatedPlace ratedPlace = call.bodyAsObject(UserRatedPlace.class);
        ratedPlace.setUserId(userId);
        ratedPlace.setPlaceId(placeId);
        ratedPlace.setCreatedMillis(null);
        ratedPlace.setUpdatedMillis(null);

        ratedPlaceClient.put(ratedPlace);
        placeRatingClient.queue(placeId, userId);
        return ratedPlace;
    }
}
