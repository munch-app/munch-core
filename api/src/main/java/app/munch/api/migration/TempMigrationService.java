package app.munch.api.migration;

import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 19/8/19
 * Time: 4:42 am
 */
@Singleton
public final class TempMigrationService implements TransportService {
    private static final Logger logger = LoggerFactory.getLogger(TempMigrationService.class);

    private final TransactionProvider provider;

    @Inject
    TempMigrationService(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void route() {
        PATH("/migrations/temp", () -> {

        });
    }

}
