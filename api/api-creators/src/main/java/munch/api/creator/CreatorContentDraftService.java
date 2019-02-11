package munch.api.creator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.restful.core.JsonUtils;
import munch.restful.core.KeyUtils;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.CreatorContentClient;
import munch.user.client.CreatorContentDraftClient;
import munch.user.client.CreatorContentItemClient;
import munch.user.data.CreatorContent;
import munch.user.data.CreatorContentDraft;
import munch.user.data.CreatorContentItem;
import munch.user.data.CreatorUser;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 23:03
 * Project: munch-core
 */
@Singleton
public final class CreatorContentDraftService extends AbstractCreatorService {

    private final CreatorContentClient contentClient;
    private final CreatorContentItemClient itemClient;
    private final CreatorContentDraftClient draftClient;

    @Inject
    public CreatorContentDraftService(CreatorContentClient contentClient, CreatorContentItemClient itemClient, CreatorContentDraftClient draftClient) {
        this.contentClient = contentClient;
        this.itemClient = itemClient;
        this.draftClient = draftClient;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void route() {
        PATH("/creators/:creatorId/contents/:contentId/drafts", () -> {
            GET("", this::get);
            POST("", this::post);
            PUT("", this::put);

            PATCH("/publish", this::publish);
        });
    }

    public JsonResult get(JsonCall call) {
        CreatorContent content = call.get(CreatorContent.class);
        final String creatorId = content.getCreatorId();

        CreatorContentDraft draft = draftClient.getLatest(creatorId, content.getContentId());
        if (draft == null) {
            draft = new CreatorContentDraft();
            draft.setType("doc");
            draft.setContent(List.of());
        }

        return JsonResult.ok(Map.of(
                "content", content,
                "draft", draft
        ));
    }

    public JsonResult post(JsonCall call) {
        final String creatorId = call.get(CreatorUser.class).getCreatorId();

        CreatorContentDraft draft = call.bodyAsObject(CreatorContentDraft.class);

        CreatorContent content = new CreatorContent();
        content.setCreatorId(creatorId);

        CreatorContentItemUtils.patchContent(content, draft);
        content = contentClient.post(content);
        draftClient.putLatest(creatorId, content.getContentId(), draft);

        return JsonResult.ok(Map.of(
                "content", content,
                "draft", draft
        ));
    }

    public JsonResult put(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String contentId = call.pathString("contentId");

        CreatorContentDraft draft = call.bodyAsObject(CreatorContentDraft.class);
        CreatorContent content = call.get(CreatorContent.class);
        content.setSortId(KeyUtils.randomMillisUUID());

        CreatorContentItemUtils.patchContent(content, draft);
        content = contentClient.patch(creatorId, contentId, JsonUtils.toTree(content));
        draftClient.putLatest(creatorId, content.getContentId(), draft);

        return JsonResult.ok(Map.of(
                "content", content,
                "draft", draft
        ));
    }

    public JsonResult publish(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String contentId = call.pathString("contentId");
        final JsonNode body = call.bodyAsJson();

        CreatorContentDraft draft = draftClient.getLatest(creatorId, contentId);
        CreatorContent content = call.get(CreatorContent.class);
        if (draft == null) return JsonResult.notFound();

        // Future: Linked Type Validation

        List<CreatorContentItem> items = CreatorContentItemUtils.getItems(draft);
        linkPlace(content, items, body);
        putItems(content, items);
        content = putContent(content, items, body);

        return JsonResult.ok(Map.of(
                "content", content,
                "draft", draft
        ));
    }

    private static void linkPlace(CreatorContent content, List<CreatorContentItem> items, JsonNode body) {
        String linkedType = body.path("linked").path("type").asText(null);
        if (StringUtils.isBlank(linkedType)) return;

        for (CreatorContentItem item : items) {
            Objects.requireNonNull(item.getBody());
            if (item.getType() != CreatorContentItem.Type.place) continue;

            String placeId = item.getBody().path("placeId").asText();
            item.setLinkedId(linkedType + "~" + placeId);
            item.setLinkedSort(KeyUtils.createUUID(content.getCreatedMillis(), RandomUtils.nextLong()));
        }
    }

    private List<CreatorContentItem> putItems(CreatorContent content, List<CreatorContentItem> items) {
        final String contentId = content.getContentId();

        for (CreatorContentItem item : items) {
            item.setContentId(contentId);
            itemClient.put(item);
        }

        // Cleaning Up Items
        itemClient.list(contentId, items.get(items.size() - 1).getItemId(), 30).toIterator(next -> {
            return itemClient.list(contentId, next.path("itemId").asText(), 30);
        }).forEachRemaining(item -> itemClient.delete(contentId, item.getItemId()));

        return items;
    }

    private CreatorContent putContent(CreatorContent content, List<CreatorContentItem> items, final JsonNode body) {
        ObjectNode patch = JsonUtils.createObjectNode();
        patch.put("title", body.path("title").asText());
        patch.put("body", body.path("body").asText());
        patch.put("subtitle", body.path("subtitle").asText());
        patch.put("status", "published");
        patch.put("platform", CreatorContentItemUtils.getPlatform(items));

        patch.set("image", body.path("image"));
        patch.set("tags", body.path("tags"));
        return contentClient.patch(content.getCreatorId(), content.getContentId(), patch);
    }
}
