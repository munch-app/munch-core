package munch.api.user;

import app.munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 5:17 PM
 * Project: munch-core
 */
public final class UserModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addDeprecatedService(UserAuthenticationService.class);
        addDeprecatedService(UserService.class);

        addDeprecatedService(UserPlaceCollectionService.class);
        addDeprecatedService(UserPlaceCollectionItemService.class);

        addDeprecatedService(UserSearchPreferenceService.class);
        addDeprecatedService(UserSavedPlaceService.class);
        addDeprecatedService(UserRecentPlaceService.class);
        addDeprecatedService(UserLocationService.class);
        addDeprecatedService(UserRatedPlaceService.class);
    }
}
