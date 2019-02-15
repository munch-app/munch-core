package munch.api.location;

import munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 2019-02-15
 * Time: 22:00
 * Project: munch-core
 */
public final class LocationModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(LocationService.class);
    }
}
