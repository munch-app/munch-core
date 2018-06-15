package munch.api.core;

import munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 14/6/18
 * Time: 12:10 AM
 * Project: munch-core
 */
public final class CoreModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addCleaner(AreaCleaner.class);
        addCleaner(TagCleaner.class);
    }
}
