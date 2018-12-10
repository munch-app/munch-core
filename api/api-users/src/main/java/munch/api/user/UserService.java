package munch.api.user;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.restful.core.exception.AuthenticationException;
import munch.restful.server.JsonCall;
import munch.user.client.UserProfileClient;
import munch.user.client.UserSettingClient;
import munch.user.data.UserProfile;
import munch.user.data.UserSetting;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 18/5/18
 * Time: 10:39 PM
 * Project: munch-core
 */
@Singleton
public final class UserService extends ApiService {
    private final UserProfileClient profileClient;
    private final UserSettingClient settingClient;

    @Inject
    public UserService(UserProfileClient profileClient, UserSettingClient settingClient) {
        this.profileClient = profileClient;
        this.settingClient = settingClient;
    }

    @Override
    public void route() {
        PATH("/users", () -> {
            GET("/profile", this::getProfile);
            GET("/setting", this::getSetting);
        });
    }

    public UserProfile getProfile(JsonCall call) {
        String userId = call.get(ApiRequest.class).getUserId();
        return profileClient.get(userId);
    }

    public UserSetting getSetting(JsonCall call) throws AuthenticationException {
        String userId = call.get(ApiRequest.class).getUserId();
        UserSetting setting = settingClient.get(userId);
        if (setting != null) return setting;
        return new UserSetting();
    }
}
