package munch.api.creator;

import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorContentItemClient;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorContentIndex;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 23:03
 * Project: munch-core
 */
@Singleton
public final class CreatorContentService extends AbstractCreatorService {

    private final CreatorContentClient contentClient;
    private final CreatorContentItemClient itemClient;

    @Inject
    public CreatorContentService(CreatorContentClient contentClient, CreatorContentItemClient itemClient) {
        this.contentClient = contentClient;
        this.itemClient = itemClient;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void route() {
        AUTHENTICATED("/creators/:creatorId/contents", () -> {
            GET("", this::list);
            POST("", this::post);

            AUTHENTICATED("/:contentId", () -> {
                GET("", this::get);
                PATCH("", this::patch);
                DELETE("", this::delete);
            });
        });
    }

    @SuppressWarnings("Duplicates")
    public NextNodeList<CreatorContent> list(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final int size = call.querySize(20, 40);

        CreatorContentIndex index = call.queryEnum("index", CreatorContentIndex.class, CreatorContentIndex.sortId);
        String next = call.queryString("next." + index.getRangeName(), null);
        return contentClient.list(index, creatorId, next, size);
    }

    public CreatorContent get(JsonCall call) {
        return call.get(CreatorContent.class);
    }

    public CreatorContent post(JsonCall call) {
        CreatorContent content = call.bodyAsObject(CreatorContent.class);
        content.setCreatorId(call.pathString("creatorId"));
        return contentClient.post(content);
    }

    public CreatorContent patch(JsonCall call) {
        String creatorId = call.pathString("creatorId");
        String contentId = call.pathString("contentId");

        return contentClient.patch(creatorId, contentId, call.bodyAsJson());
    }

    public CreatorContent delete(JsonCall call) {
        String creatorId = call.pathString("creatorId");
        String contentId = call.pathString("contentId");

        // Remove all item inside
        itemClient.iterator(contentId, 30).forEachRemaining(item -> {
            itemClient.delete(item.getContentId(), item.getItemId());
        });

        return contentClient.delete(creatorId, contentId);
    }
}
