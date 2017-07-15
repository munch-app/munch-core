package munch.api.services;

import com.google.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2/7/2017
 * Time: 3:16 AM
 * Project: munch-core
 */
@Singleton
public class CachedSyncService extends AbstractService {

    @Override
    public void route() {
        PATH("/cached/sync", () -> {
            // PD-14 Sync service for fetching settings
        });
    }
}
