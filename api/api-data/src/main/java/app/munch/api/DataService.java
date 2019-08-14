package app.munch.api;

import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportService;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 11:55
 */
public abstract class DataService implements TransportService {

    protected final TransactionProvider provider;

    protected DataService(TransactionProvider provider) {
        this.provider = provider;
    }
}
