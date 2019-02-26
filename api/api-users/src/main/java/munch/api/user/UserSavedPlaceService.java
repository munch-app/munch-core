package munch.api.user;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSavedPlaceClient;
import munch.user.data.UserSavedPlace;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 9/12/18
 * Time: 11:14 PM
 * Project: munch-core
 */
@Singleton
public final class UserSavedPlaceService extends ApiService {

    private final UserSavedPlaceClient savedPlaceClient;
    private final PlaceCachedClient placeClient;

    @Inject
    public UserSavedPlaceService(UserSavedPlaceClient savedPlaceClient, PlaceCachedClient placeClient) {
        this.savedPlaceClient = savedPlaceClient;
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        PATH("/users/saved/places", () -> {
            GET("", this::list);
            PUT("/:placeId", this::put);
            DELETE("/:placeId", this::delete);
        });
    }

    public NextNodeList list(JsonCall call) {
        String userId = call.get(ApiRequest.class).getUserId();
        Long next = call.queryObject("next.createdMillis", null, Long.class);
        int size = call.querySize(20, 40);

        NextNodeList<UserSavedPlace> nnList = savedPlaceClient.list(userId, next, size);
        Map<String, Place> placeMap = placeClient.get(nnList.stream().map(UserSavedPlace::getPlaceId));

        List<Map> places = nnList.stream()
                .filter(usp -> placeMap.get(usp.getPlaceId()) != null)
                .map(usp -> Map.of(
                        "userId", usp.getUserId(),
                        "placeId", usp.getPlaceId(),
                        "name", usp.getName(),
                        "createdMillis", usp.getCreatedMillis(),
                        "place", placeMap.get(usp.getPlaceId())
                )).collect(Collectors.toList());

        return new NextNodeList<>(places, nnList.getNext());
    }

    public JsonResult put(JsonCall call) {
        String userId = call.get(ApiRequest.class).getUserId();
        String placeId = call.pathString("placeId");

        Place place = placeClient.get(placeId);
        if (place == null) return JsonResult.notFound();

        UserSavedPlace savedPlace = new UserSavedPlace();
        savedPlace.setPlaceId(placeId);
        savedPlace.setName(place.getName());

        savedPlaceClient.put(userId, savedPlace);
        return JsonResult.ok();
    }

    @Nullable
    public UserSavedPlace delete(JsonCall call) {
        String userId = call.get(ApiRequest.class).getUserId();
        String placeId = call.pathString("placeId");
        return savedPlaceClient.delete(userId, placeId);
    }
}
