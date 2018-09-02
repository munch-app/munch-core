package munch.api.place;

import munch.api.ApiServiceModule;
import munch.instagram.InstagramCleaner;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 5:21 PM
 * Project: munch-core
 */
public final class PlaceModule extends ApiServiceModule {
    @Override
    protected void configure() {
        addCleaner(InstagramCleaner.class);

        addService(PlaceService.class);
        addService(PlacePartnerService.class);
    }
}
