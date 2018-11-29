package munch.api.user;

import munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 5:17 PM
 * Project: munch-core
 */
public final class UserModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(UserService.class);
        addService(UserPlaceCollectionService.class);
        addService(UserPlaceCollectionItemService.class);
        addService(UserPlaceActivityService.class);

        addService(UserSuggestService.class);
    }
}
