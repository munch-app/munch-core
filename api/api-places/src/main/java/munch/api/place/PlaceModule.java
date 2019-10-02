package munch.api.place;

import app.munch.api.ApiServiceModule;
import munch.article.ArticleCleaner;
import munch.instagram.InstagramCleaner;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 5:21 PM
 * Project: munch-core
 */
@Deprecated
public final class PlaceModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addCleaner(InstagramCleaner.class);
        addCleaner(ArticleCleaner.class);

        addDeprecatedService(PlaceService.class);
    }
}
