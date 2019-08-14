package munch.api.feed;

import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.feed.FeedItem;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2019-02-22
 * Time: 17:54
 * Project: munch-core
 */
@Singleton
public final class FeedQueryService extends ApiService {

    private final PlaceCachedClient placeClient;
    private final FeedQueryDelegator queryDelegator;

    @Inject
    public FeedQueryService(PlaceCachedClient placeClient, FeedQueryDelegator queryDelegator) {
        this.placeClient = placeClient;
        this.queryDelegator = queryDelegator;
    }

    @Override
    public void route() {
        PATH("/feed/query", () -> {
            POST("", this::query);
        });
    }

    public JsonResult query(JsonCall call, ApiRequest request) {
        FeedQuery query = call.bodyAsObject(FeedQuery.class);
        NextNodeList<FeedItem> items = queryDelegator.query(query, call, request);

        Map<String, Place> places = getPlaces(items);
        validateItems(items, places);

        return JsonResult.ok(items)
                .put("places", places);
    }

    /**
     * @param items to get places from
     * @return Map of Place
     */
    private Map<String, Place> getPlaces(NextNodeList<FeedItem> items) {
        Set<String> placeIds = new HashSet<>();
        items.forEach(item -> item.getPlaces().forEach(place -> {
            placeIds.add(place.getPlaceId());
        }));
        return placeClient.get(placeIds);
    }

    private void validateItems(NextNodeList<FeedItem> items, Map<String, Place> places) {
        items.removeIf(item -> {
            for (FeedItem.Place p : item.getPlaces()) {
                Place place = places.get(p.getPlaceId());
                if (place == null) return true;
                if (place.getStatus().getType() != Place.Status.Type.open) return true;
            }
            return false;
        });
    }
}
