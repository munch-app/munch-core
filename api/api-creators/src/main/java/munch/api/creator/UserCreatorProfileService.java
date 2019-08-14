package munch.api.creator;

import app.munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.user.client.CreatorProfileClient;
import munch.user.client.UserCreatorClient;
import munch.user.data.CreatorProfile;
import munch.user.data.CreatorUser;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 19:16
 * Project: munch-core
 */
@Singleton
public final class UserCreatorProfileService extends ApiService {

    private final UserCreatorClient userCreatorClient;
    private final CreatorProfileClient creatorProfileClient;

    @Inject
    public UserCreatorProfileService(UserCreatorClient userCreatorClient, CreatorProfileClient creatorProfileClient) {
        this.userCreatorClient = userCreatorClient;
        this.creatorProfileClient = creatorProfileClient;
    }

    @Override
    public void route() {
        PATH("/users/creators/profiles", () -> {
            GET("", this::list);
            POST("", this::post);
        });
    }

    public NextNodeList<CreatorProfile> list(JsonCall call, ApiRequest request) {
        final String userId = request.getAccountId();

        NextNodeList<CreatorUser> creatorUsers = userCreatorClient.list(userId, null, call.querySize(5, 10));
        return creatorUsers
                .map(creatorUser -> creatorProfileClient.get(creatorUser.getCreatorId()));
    }

    public CreatorProfile post(JsonCall call, ApiRequest request) {
        final String userId = request.getAccountId();

        CreatorProfile profile = call.bodyAsObject(CreatorProfile.class);
        return userCreatorClient.post(userId, profile);
    }
}
