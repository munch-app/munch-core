package munch.api;

import com.google.common.collect.ImmutableSet;
import com.typesafe.config.Config;
import munch.api.exception.UnsupportedException;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
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

    private final HealthService healthService;
    private final VersionService versionService;
    private final ValidateVersion validateVersion;

    @Inject
    public ApiServer(Set<RestfulService> routers, HealthService healthService, VersionService versionService, Config config) {
        super(routers);
        this.healthService = healthService;
        this.versionService = versionService;
        this.validateVersion = new ValidateVersion(config);
    }

    @Override
    protected void setupRouters() {
        // Validate that version is supported
        Spark.before((req, res) -> validateVersion.validate(req));
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

    /**
     * Created By: Fuxing Loh
     * Date: 23/7/2017
     * Time: 4:03 PM
     * Project: munch-core
     */
    private final class ValidateVersion {
        public static final String HEADER_VERSION = "Application-Version";
        public static final String HEADER_BUILD = "Application-Build";

        private final ImmutableSet<String> supportedVersions;

        public ValidateVersion(Config config) {
            String versions = config.getString("api.versions.supported");
            this.supportedVersions = ImmutableSet.copyOf(versions.split(" *, *"));
        }

        public void validate(Request request) {
            String version = request.headers(HEADER_VERSION);
            if (StringUtils.isNotBlank(version) && !supportedVersions.contains(version)) {
                throw new UnsupportedException();
            }

            // If version is not given, no error will be thrown
        }
    }
}
