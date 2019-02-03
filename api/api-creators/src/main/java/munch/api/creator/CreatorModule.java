package munch.api.creator;

import munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 19:15
 * Project: munch-core
 */
public final class CreatorModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(UserCreatorProfileService.class);

        // CreatorId based Service
        addService(CreatorStoryService.class);
        addService(CreatorSeriesService.class);
        addService(CreatorUserService.class);
    }
}
