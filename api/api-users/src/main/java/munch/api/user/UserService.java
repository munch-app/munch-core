package munch.api.user;

import com.google.firebase.auth.FirebaseToken;
import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.notification.MunchMailingListClient;
import munch.restful.core.exception.AuthenticationException;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.restful.server.firebase.FirebaseAuthenticatedToken;
import munch.restful.server.firebase.FirebaseAuthenticator;
import munch.user.client.UserProfileClient;
import munch.user.client.UserSettingClient;
import munch.user.data.UserProfile;
import munch.user.data.UserSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 18/5/18
 * Time: 10:39 PM
 * Project: munch-core
 */
@Singleton
public final class UserService extends ApiService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final FirebaseAuthenticator authenticator;

    private final UserProfileClient profileClient;
    private final UserSettingClient settingClient;

    private final MunchMailingListClient munchMailingListClient;

    @Inject
    public UserService(FirebaseAuthenticator authenticator, UserProfileClient profileClient, UserSettingClient settingClient, MunchMailingListClient munchMailingListClient) {
        this.authenticator = authenticator;
        this.profileClient = profileClient;
        this.settingClient = settingClient;
        this.munchMailingListClient = munchMailingListClient;
    }

    @Override
    public void route() {
        PATH("/users", () -> {
            GET("/profile", this::getProfile);
            GET("/setting", this::getSetting);

            POST("/authenticate", this::authenticate);
            PATCH("/setting/search", this::patchSearchSetting);
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

    /**
     * @param call json call
     * @return UserProfile & UserSetting after authentication
     */
    public JsonResult authenticate(JsonCall call) {
        FirebaseAuthenticatedToken token = authenticator.authenticate(call);
        String userId = token.getSubject();

        UserProfile profile = updateProfile(userId, token.getFirebaseToken());
        UserSetting setting = updateSetting(userId, profile);

        return result(200, Map.of(
                "profile", profile,
                "setting", setting
        ));
    }

    public UserSetting patchSearchSetting(JsonCall call) {
        String userId = call.get(ApiRequest.class).getUserId();
        UserSetting setting = settingClient.get(userId);
        if (setting == null) setting = new UserSetting();

        setting.setSearch(call.bodyAsObject(UserSetting.Search.class));
        settingClient.put(userId, setting);
        return setting;
    }

    private UserProfile updateProfile(String userId, FirebaseToken firebaseToken) {
        UserProfile profile = profileClient.get(userId);

        UserProfile updatedProfile = new UserProfile();
        updatedProfile.setUserId(userId);
        updatedProfile.setEmail(firebaseToken.getEmail());
        updatedProfile.setName(firebaseToken.getName());
        updatedProfile.setPhotoUrl(firebaseToken.getPicture());

        // If update profile no change, don't need to update
        if (updatedProfile.equals(profile)) return updatedProfile;

        profileClient.put(userId, updatedProfile);
        return updatedProfile;
    }

    private UserSetting updateSetting(String userId, UserProfile profile) {
        UserSetting setting = settingClient.get(userId);
        if (setting != null) return setting;

        try {
            munchMailingListClient.post(profile.getEmail(), profile.getName());
        } catch (StructuredException e) {
            logger.warn("Failed to add user: {}, email: {} to munch mailing list.", userId, profile.getEmail(), e);
        }

        setting = new UserSetting();
        setting.setUserId(userId);
        setting.setMailings(Map.of("munch", true));
        setting.setSearch(new UserSetting.Search());
        settingClient.put(userId, setting);
        return setting;
    }
}
