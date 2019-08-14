package munch.api.user;

import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserRecentPlaceClient;
import munch.user.data.UserRecentPlace;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 9/12/18
 * Time: 11:14 PM
 * Project: munch-core
 */
@Singleton
public final class UserRecentPlaceService extends ApiService {

    private final UserRecentPlaceClient recentPlaceClient;
    private final PlaceCachedClient placeClient;

    @Inject
    public UserRecentPlaceService(UserRecentPlaceClient recentPlaceClient, PlaceCachedClient placeClient) {
        this.recentPlaceClient = recentPlaceClient;
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        PATH("/users/recent/places", () -> {
            PUT("/:placeId", this::put);
        });
    }

    public JsonResult put(JsonCall call) {
        String userId = call.get(ApiRequest.class).getAccountId();
        String placeId = call.pathString("placeId");

        Place place = placeClient.get(placeId);
        if (place == null) return JsonResult.notFound();

        UserRecentPlace recentPlace = new UserRecentPlace();
        recentPlace.setPlaceId(placeId);
        recentPlace.setName(place.getName());

        recentPlaceClient.put(userId, recentPlace);
        return JsonResult.ok();
    }
}
