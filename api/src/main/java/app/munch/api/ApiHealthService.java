package app.munch.api;

import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 8:46 AM
 * Project: munch-core
 */
@Singleton
public final class ApiHealthService implements TransportService {
    private static final Logger logger = LoggerFactory.getLogger(ApiHealthService.class);

    private final Set<ApiHealthCheck> healthChecks;

    @Inject
    public ApiHealthService(Set<ApiHealthCheck> healthChecks) {
        this.healthChecks = healthChecks;
    }

    @Override
    public void route() {
        GET("/health/check", this::check);
    }

    private TransportResult check(TransportContext ctx) throws Exception {
        try {
            for (ApiHealthCheck healthCheck : healthChecks) {
                healthCheck.check();
            }
        } catch (Exception e) {
            logger.error("Health Check Error", e);
            throw e;
        }
        return TransportResult.ok();
    }
}
