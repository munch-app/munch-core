package munch.api.services;

import com.google.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 8:11 AM
 * Project: munch-core
 */
@Singleton
public class MetaService extends AbstractService {

    @Override
    public void route() {
        PATH("/meta", () -> {
            // For use for alpha/beta channel testing
        });
    }

}
