package munch.api.contents;

import app.munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 2019-02-12
 * Time: 19:17
 * Project: munch-core
 */
@Deprecated
public final class ContentModule extends ApiServiceModule {

    @Override
    protected void configure() {
        addDeprecatedService(ContentService.class);
    }
}
