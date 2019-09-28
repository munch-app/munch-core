package app.munch.api;

import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportService;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 11:55
 */
public abstract class DataService implements TransportService {

    protected TransactionProvider provider;

    @Deprecated
    protected DataService(TransactionProvider provider) {
    }

    protected DataService() {
    }

    @Inject
    void inject(TransactionProvider provider) {
        this.provider = provider;

    }
}
