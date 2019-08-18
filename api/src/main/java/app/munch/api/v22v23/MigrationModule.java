package app.munch.api.v22v23;

import app.munch.api.ApiServiceModule;

/**
 * Created by: Fuxing
 * Date: 19/8/19
 * Time: 5:00 am
 */
public class MigrationModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addService(MigrationService.class);
    }
}
