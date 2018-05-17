package munch.api;

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

        setDebug(false);
    }

    @Override
    protected void setupRouters() {
        // Added /* because beginning of path has version number
        Spark.path("/*", () -> super.setupRouters());

        healthService.start();
        logger.info("Started SparkRouter: HealthService");
    }
}
