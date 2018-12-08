package munch.api.feed;

import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.feed.FeedItem;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonResult;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 11/10/18
 * Time: 2:15 PM
 * Project: munch-core
 */
public abstract class FeedService extends ApiService {
    protected final PlaceCachedClient placeClient;

    @Inject
    public FeedService(PlaceCachedClient placeClient) {
        this.placeClient = placeClient;
    }

    /**
     * @param feedItem to convert to json for delivery
     * @return data: {item: {}, places: {placeId: {}}}
     */
    protected JsonResult asResult(FeedItem feedItem) {
        Set<String> placeIds = new HashSet<>();
        feedItem.getPlaces().forEach(place -> {
            placeIds.add(place.getPlaceId());
        });

        return JsonResult.ok(Map.of(
                "item", feedItem,
                "places", placeClient.get(placeIds)
        ));
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
