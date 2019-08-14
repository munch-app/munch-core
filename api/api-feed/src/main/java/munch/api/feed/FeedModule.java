package munch.api.feed;

import app.munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 11/10/18
 * Time: 2:46 PM
 * Project: munch-core
 */
public final class FeedModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addDeprecatedService(FeedItemService.class);
        addDeprecatedService(FeedQueryService.class);
    }
}
