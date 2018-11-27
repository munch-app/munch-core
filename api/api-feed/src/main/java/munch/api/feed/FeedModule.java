package munch.api.feed;

import munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 11/10/18
 * Time: 2:46 PM
 * Project: munch-core
 */
public final class FeedModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(FeedImageService.class);
        addService(FeedArticleService.class);
    }
}
