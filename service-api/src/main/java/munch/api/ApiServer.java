package munch.api;

import munch.api.exception.UnsupportedException;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * FYI: For error handling use Structured Error
 * <p>
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public final class ApiServer extends RestfulServer {
    private static final Logger logger = LoggerFactory.getLogger(ApiServer.class);

    private final HealthService healthService;

    @Inject
    public ApiServer(Set<RestfulService> routers, HealthService healthService) {
        super(routers);
        this.healthService = healthService;
    }

    @Override
    public void start() {
        super.start();

        // Only log non health check path
        Spark.before((request, response) -> {
            if (!request.pathInfo().equals("/health/check"))
                logger.trace("{}: {}", request.requestMethod(), request.pathInfo());
        });
    }

    @Override
    protected void setupRouters() {
        // Added /* because beginning of path has version number
        Spark.path("/*", () -> super.setupRouters());

        healthService.start();
        logger.info("Started SparkRouter: VersionService");
    }

    @Override
    protected void handleException() {
        // UnsupportedException is handled but not logged
        logger.info("Adding exception handling for UnsupportedException.");
        Spark.exception(UnsupportedException.class, (exception, request, response) -> {
            handleException(response, (StructuredException) exception);
        });
        super.handleException();
    }
}
