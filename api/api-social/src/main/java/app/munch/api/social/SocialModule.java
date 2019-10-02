package app.munch.api.social;

import app.munch.api.ApiServiceModule;

/**
 * Date: 2/10/19
 * Time: 1:24 pm
 *
 * @author Fuxing Loh
 */
public final class SocialModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(MeSocialInstagramService.class);
    }
}
