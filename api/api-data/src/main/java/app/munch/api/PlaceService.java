package app.munch.api;

import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class PlaceService extends DataService {

    @Inject
    PlaceService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/places/:id", () -> {
            PATH("/revisions", () -> {
            });
        });
    }
}
