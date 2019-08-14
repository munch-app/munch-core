package munch.api.contents;

import api.munch.migration.ContentMigration;
import munch.api.ApiService;
import munch.data.client.PlaceCachedClient;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorContentItemClient;
import munch.user.client.CreatorProfileClient;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorContentItem;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 2019-02-12
 * Time: 19:16
 * Project: munch-core
 */
@Singleton
@Deprecated
public final class ContentService extends ApiService {

    private final CreatorContentClient contentClient;
    private final CreatorProfileClient profileClient;
    private final CreatorContentItemClient itemClient;
    private final PlaceCachedClient placeClient;

    private final ContentMigration migration;

    @Inject
    public ContentService(CreatorContentClient contentClient, CreatorProfileClient profileClient, CreatorContentItemClient itemClient, PlaceCachedClient placeClient, ContentMigration migration) {
        this.contentClient = contentClient;
        this.profileClient = profileClient;
        this.itemClient = itemClient;
        this.placeClient = placeClient;
        this.migration = migration;
    }

    @Override
    public void route() {
        GET("/contents-migration", (call, request) -> {
            return migration.download();
        });

        PATH("/contents/:contentId", () -> {
            GET("", this::get);

            PATH("/items", () -> {
                GET("", this::listItems);
            });
        });
    }

    public CreatorContent get(JsonCall call) {
        final String contentId = call.pathString("contentId");
        CreatorContent content = contentClient.get(contentId);
        if (content == null) return null;

        content.setProfile(profileClient.get(content.getCreatorId()));
        return content;
    }

    public JsonResult listItems(JsonCall call) {
        final String contentId = call.pathString("contentId");
        final int size = call.querySize(30, 50);
        final String itemId = call.queryString("next.itemId", null);

        NextNodeList<CreatorContentItem> items = itemClient.list(contentId, itemId, size);
        Stream<String> placeIds = items.stream()
                .filter(item -> item.getType() == CreatorContentItem.Type.place)
                .map(item -> Objects.requireNonNull(item.getBody()).path("placeId").asText());


        return JsonResult.ok(items)
                .put("places", placeClient.get(placeIds));
    }
}
