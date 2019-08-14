package app.munch.api;

import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportService;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 14:25
 */
public abstract class AdminService implements TransportService {
    protected final TransactionProvider provider;

    protected AdminService(TransactionProvider provider) {
        this.provider = provider;
    }
}
