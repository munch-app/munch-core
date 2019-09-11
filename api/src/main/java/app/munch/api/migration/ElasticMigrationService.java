package app.munch.api.migration;

import app.munch.elastic.ElasticMapping;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 2:37 pm
 */
@Singleton
public final class ElasticMigrationService implements TransportService {

    private final ElasticMapping mapping;

    @Inject
    ElasticMigrationService(ElasticMapping mapping) {
        this.mapping = mapping;
    }

    @Override
    public void route() {
        PATH("/admin/migrations/elastic", () -> {
//            POST("/setup", this::setup);
        });
    }

    public TransportResult setup(TransportContext ctx) throws IOException {
        mapping.create();
        return TransportResult.ok();
    }
}

