package munch.api.creator;

import munch.restful.core.NextNodeList;
import munch.restful.core.exception.BadRequestException;
import munch.restful.core.exception.ForbiddenException;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorStoryClient;
import munch.user.client.CreatorStoryClient.ListMethod;
import munch.user.client.CreatorStoryItemClient;
import munch.user.data.CreatorStory;
import munch.user.data.CreatorStoryItem;
import munch.user.data.MunchCreatorId;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 23:03
 * Project: munch-core
 */
@Singleton
public final class CreatorStoryService extends AbstractCreatorService {

    private final CreatorStoryClient storyClient;
    private final CreatorStoryItemClient storyItemClient;

    @Inject
    public CreatorStoryService(CreatorStoryClient storyClient, CreatorStoryItemClient storyItemClient) {
        this.storyClient = storyClient;
        this.storyItemClient = storyItemClient;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void route() {
        PATH("/creators/:creatorId/stories", () -> {
            BEFORE("", this::authenticateCreator);

            GET("", this::list);
            POST("", this::post);

            PATH("/:storyId", () -> {
                BEFORE("", this::authenticateStory);

                GET("", this::get);
                PATCH("", this::patch);
                DELETE("", this::delete);
                PATH("/items", () -> {
                    GET("", this::listItem);
                    POST("", this::postItem);
                    PATCH("/:itemId", this::patchItem);
                    DELETE("/:itemId", this::deleteItem);
                });
            });
        });
    }

    @SuppressWarnings("Duplicates")
    public NextNodeList<CreatorStory> list(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final int size = call.querySize(20, 40);

        ListMethod method = call.queryEnum("sort", ListMethod.class, ListMethod.sort);
        String next = call.queryString("next." + method.nextName(), null);
        return storyClient.list(method, creatorId, next, size);
    }

    public CreatorStory get(JsonCall call) {
        String storyId = call.pathString("storyId");

        CreatorStory story = storyClient.get(storyId);
        if (story == null) throw new ForbiddenException("Creator Forbidden");

        authenticateCreator(call, story.getCreatorId());
        return story;
    }

    public CreatorStory post(JsonCall call) {
        CreatorStory story = call.bodyAsObject(CreatorStory.class);
        story.setCreatorId(call.pathString("creatorId"));
        return storyClient.post(story);
    }

    public CreatorStory patch(JsonCall call) {
        String creatorId = call.pathString("creatorId");
        String storyId = call.pathString("storyId");

        return storyClient.patch(creatorId, storyId, call.bodyAsJson());
    }

    public CreatorStory delete(JsonCall call) {
        String creatorId = call.pathString("creatorId");
        String storyId = call.pathString("storyId");
        return storyClient.delete(creatorId, storyId);
    }

    public NextNodeList<CreatorStoryItem> listItem(JsonCall call) {
        final String storyId = call.pathString("storyId");
        final int size = call.querySize(20, 40);

        CreatorStoryItemClient.ListMethod method = call.queryEnum("sort", CreatorStoryItemClient.ListMethod.class, CreatorStoryItemClient.ListMethod.sort);
        String next = call.queryString("next." + method.nextName(), null);
        return storyItemClient.list(method, storyId, next, size);
    }

    public CreatorStoryItem postItem(JsonCall call) {
        CreatorStoryItem item = call.bodyAsObject(CreatorStoryItem.class);

        // CreatorStoryItem.Type.html is only available for Munch Creators.
        if (item.getType() == CreatorStoryItem.Type.html) {
            if (!MunchCreatorId.is(call.pathString("creatorId"))) {
                throw new BadRequestException("Type.html is only available for Munch Creator.");
            }
        }

        item.setStoryId(call.pathString("storyId"));
        return storyItemClient.post(item);
    }

    public CreatorStoryItem patchItem(JsonCall call) {
        final String storyId = call.pathString("storyId");
        final String itemId = call.pathString("itemId");
        return storyItemClient.patch(storyId, itemId, call.bodyAsJson());
    }

    public CreatorStoryItem deleteItem(JsonCall call) {
        final String storyId = call.pathString("storyId");
        final String itemId = call.pathString("itemId");
        return storyItemClient.delete(storyId, itemId);
    }
}
