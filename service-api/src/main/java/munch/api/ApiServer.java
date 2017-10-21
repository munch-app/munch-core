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
    private final VersionService versionService;
    private final VersionService.Validator versionValidator;

    @Inject
    public ApiServer(Set<RestfulService> routers, HealthService healthService, VersionService versionService, VersionService.Validator versionValidator) {
        super(routers);
        this.healthService = healthService;
        this.versionService = versionService;
        this.versionValidator = versionValidator;
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
        // Validate that version is supported
        Spark.before((req, res) -> versionValidator.validate(req));
        logger.info("Added version validator to all routes");

        super.setupRouters();

        healthService.start();
        logger.info("Started SparkRouter: VersionService");

        versionService.start();
        logger.info("Started SparkRouter: HealthService");
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
