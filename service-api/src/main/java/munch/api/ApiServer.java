package munch.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;
import spark.Spark;

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

    private final Config config;
    private final SupportedVersions supportedVersions;
    private final HealthService healthService;

    @Inject
    public ApiServer(HealthService healthService, Set<RestfulService> routers, Config config, SupportedVersions supportedVersions) {
        super(routers);
        this.config = config;
        this.supportedVersions = supportedVersions;
        this.healthService = healthService;
    }

    @Override
    protected void setupRouters() {
        // Validate that version is supported
        Spark.before((req, res) -> supportedVersions.validate(req));

        Spark.path(config.getString("api.version"), () -> super.setupRouters());

        healthService.start();
        logger.info("Started SparkRouter: HealthService");
    }
}
