package munch.api.feed;

import app.munch.api.ApiRequest;
import com.fasterxml.jackson.databind.JsonNode;
import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.feed.FeedItemClient;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Inject
    public FeedItemService(FeedItemClient itemClient, PlaceCachedClient placeClient) {
        this.itemClient = itemClient;
        this.placeClient = placeClient;
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
                "savedPlaces", List.of()
        ));
    }
}
