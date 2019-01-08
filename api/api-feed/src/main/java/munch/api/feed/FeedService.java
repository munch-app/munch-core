package munch.api.feed;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.feed.FeedItem;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSavedPlaceClient;
import munch.user.data.UserSavedPlace;

import javax.inject.Inject;
import java.util.*;

/**
 * Created by: Fuxing
 * Date: 11/10/18
 * Time: 2:15 PM
 * Project: munch-core
 */
public abstract class FeedService extends ApiService {
    protected final PlaceCachedClient placeClient;
    protected final UserSavedPlaceClient savedPlaceClient;

    @Inject
    public FeedService(PlaceCachedClient placeClient, UserSavedPlaceClient savedPlaceClient) {
        this.placeClient = placeClient;
        this.savedPlaceClient = savedPlaceClient;
    }

    /**
     * @param feedItem to convert to json for delivery
     * @return data: {item: {}, places: {placeId: {}}}
     */
    protected JsonResult asResult(JsonCall call, FeedItem feedItem) {
        Set<String> placeIds = new HashSet<>();
        feedItem.getPlaces().forEach(place -> {
            placeIds.add(place.getPlaceId());
        });


        return JsonResult.ok(Map.of(
                "item", feedItem,
                "places", placeClient.get(placeIds),
                "savedPlaces",getSavedPlaces(call, placeIds)
        ));
    }

    private List<UserSavedPlace> getSavedPlaces(JsonCall call, Set<String> placeIds) {
        ApiRequest request = call.get(ApiRequest.class);

        if (!request.optionalUserId().isPresent()) return List.of();
        String userId = request.optionalUserId().get();

        List<UserSavedPlace> savedPlaces = new ArrayList<>();
        placeIds.forEach(placeId -> {
            savedPlaces.add(savedPlaceClient.get(userId, placeId));
        });
        return savedPlaces;
    }


    /**
     * @param items to convert to json for delivery
     * @return data: {items: [], places: {placeId: {}}}
     */
    protected JsonResult result(NextNodeList<? extends FeedItem> items) {
        // Collect placeIds
        Set<String> placeIds = new HashSet<>();
        items.forEach(item -> item.getPlaces().forEach(place -> {
            placeIds.add(place.getPlaceId());
        }));

        Map<String, Place> places = placeClient.get(placeIds);

        JsonResult result = JsonResult.ok(Map.of(
                "items", items,
                "places", places
        ));

        items.removeIf(item -> {
            for (String placeId : places.keySet()) {
                if (places.get(placeId) == null) return true;
            }
            return false;
        });

        if (items.hasNext()) {
            result.put("next", items.getNext());
        }
        return result;
    }
}
