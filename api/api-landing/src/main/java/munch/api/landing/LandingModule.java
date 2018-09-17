package munch.api.landing;

import munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 3:01 PM
 * Project: munch-core
 */
public final class LandingModule extends ApiServiceModule {

    @Override
    protected void configure() {
        addService(LandingApi.class);
    }
}
