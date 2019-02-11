package munch.api.creator;

import munch.restful.core.NextNodeList;
import munch.restful.core.exception.ForbiddenException;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorUserClient;
import munch.user.data.CreatorUser;
import munch.user.data.CreatorUserIndex;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-03
 * Time: 18:03
 * Project: munch-core
 */
@Singleton
public final class CreatorUserService extends AbstractCreatorService {

    private final CreatorUserClient creatorUserClient;

    @Inject
    public CreatorUserService(CreatorUserClient creatorUserClient) {
        this.creatorUserClient = creatorUserClient;
    }

    @Override
    public void route() {
        PATH("/creators/:creatorId/users", () -> {
            GET("", this::list);

            PATH("/:userId", () -> {
                BEFORE("", this::authenticateAdmin);

                POST("", this::post);
                PATCH("", this::patch);
                DELETE("", this::delete);
            });
        });
    }

    public NextNodeList<CreatorUser> list(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final int size = call.querySize(20, 50);

        CreatorUserIndex index = call.queryEnum("index", CreatorUserIndex.class, CreatorUserIndex.userId);
        String next = call.queryString("next." + index.nextName(), null);
        return creatorUserClient.list(index, creatorId, next, size);
    }

    private void authenticateAdmin(JsonCall call) {
        CreatorUser user = call.get(CreatorUser.class);
        if (user.getRole() != CreatorUser.Role.admin) {
            throw new ForbiddenException("CreatorUser.Role.admin required.");
        }
    }

    public CreatorUser post(JsonCall call) {
        CreatorUser user = call.bodyAsObject(CreatorUser.class);
        user.setCreatorId(call.pathString("creatorId"));
        user.setUserId(call.pathString("userId"));
        return creatorUserClient.post(user);
    }

    public CreatorUser patch(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String userId = call.pathString("userId");
        return creatorUserClient.patch(creatorId, userId, call.bodyAsJson());
    }

    public CreatorUser delete(JsonCall call) {
        final String creatorId = call.pathString("creatorId");
        final String userId = call.pathString("userId");
        return creatorUserClient.delete(creatorId, userId);
    }
}
