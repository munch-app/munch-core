package munch.api.creator;

import app.munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 2019-02-02
 * Time: 19:15
 * Project: munch-core
 */
public final class CreatorModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addDeprecatedService(UserCreatorProfileService.class);

        // CreatorId based Service
        addDeprecatedService(CreatorContentService.class);
        addDeprecatedService(CreatorContentDraftService.class);
        addDeprecatedService(CreatorSeriesService.class);
        addDeprecatedService(CreatorUserService.class);

        addDeprecatedService(CreatorImageService.class);
    }
}
