package munch.api.feed;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.feed.FeedItemClient;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSavedPlaceClient;
import munch.user.data.UserSavedPlace;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/**
 * Created by: Fuxing
 * Date: 2019-02-22
 * Time: 17:55
 * Project: munch-core
 */
@Singleton
public final class FeedItemService extends ApiService {
    private final FeedItemClient itemClient;
    private final PlaceCachedClient placeClient;
    private final UserSavedPlaceClient savedClient;

    @Inject
    public FeedItemService(FeedItemClient itemClient, PlaceCachedClient placeClient, UserSavedPlaceClient savedClient) {
        this.itemClient = itemClient;
        this.placeClient = placeClient;
        this.savedClient = savedClient;
    }

    @Override
    public void route() {
        PATH("/feed/items", () -> {
            GET("/:itemId", this::get);
        });
    }

    public JsonResult get(JsonCall call, ApiRequest request) {
        final String itemId = call.pathString("itemId");
        JsonNode feedItem = itemClient.get(itemId);

        if (feedItem == null) return JsonResult.notFound();
        Set<String> placeIds = new HashSet<>();
        feedItem.path("places").forEach(node -> {
            placeIds.add(node.path("placeId").asText());
        });

        return JsonResult.ok(Map.of(
                "item", feedItem,
                "places", placeClient.get(placeIds),
                "savedPlaces", getSavedPlaces(request, placeIds)
        ));
    }

    private List<UserSavedPlace> getSavedPlaces(ApiRequest request, Set<String> placeIds) {
        if (!request.optionalUserId().isPresent()) return List.of();
        String userId = request.optionalUserId().get();

        List<UserSavedPlace> savedPlaces = new ArrayList<>();
        placeIds.forEach(placeId -> {
            savedPlaces.add(savedClient.get(userId, placeId));
        });
        return savedPlaces;
    }
}
