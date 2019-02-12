package munch.api.contents;

import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorContentItemClient;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorContentItem;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 2019-02-12
 * Time: 19:16
 * Project: munch-core
 */
@Singleton
public final class ContentService extends ApiService {

    private final CreatorContentClient contentClient;
    private final CreatorContentItemClient itemClient;
    private final PlaceCachedClient placeClient;

    @Inject
    public ContentService(CreatorContentClient contentClient, CreatorContentItemClient itemClient, PlaceCachedClient placeClient) {
        this.contentClient = contentClient;
        this.itemClient = itemClient;
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        PATH("/contents/:contentId", () -> {
            GET("", this::get);

            PATH("/items", () -> {
                GET("", this::listItems);
            });
        });
    }

    public CreatorContent get(JsonCall call) {
        final String contentId = call.pathString("contentId");
        return contentClient.get(contentId);
    }

    public JsonResult listItems(JsonCall call) {
        final String contentId = call.pathString("contentId");
        final int size = call.querySize(30, 50);
        final String itemId = call.queryString("next.itemId", null);

        NextNodeList<CreatorContentItem> items = itemClient.list(contentId, itemId, size);
        Stream<String> placeIds = items.stream()
                .filter(item -> item.getType() == CreatorContentItem.Type.place)
                .map(item -> Objects.requireNonNull(item.getBody()).path("placeId").asText());

        Map<String, Place> places = placeClient.get(placeIds);

        return JsonResult.ok(items)
                .put("places", places);
    }
}
