package munch.api;

import munch.restful.core.exception.AuthenticationException;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.JsonCall;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;
import munch.restful.server.jwt.TokenAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
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
    private final ApiAuthenticator apiAuthenticator;

    @Inject
    public ApiServer(Set<RestfulService> routers, HealthService healthService, ApiAuthenticator apiAuthenticator) {
        super(routers);
        this.healthService = healthService;
        this.apiAuthenticator = apiAuthenticator;
        setDebug(false);
    }

    @Override
    protected void setupRouters() {
        // Added /* because beginning of path has version number
        Spark.path("/*", () -> {
            Spark.before("/*", this::before);
            super.setupRouters();
        });

        healthService.start();
        logger.info("Started SparkRouter: HealthService");
    }

    /**
     * Before all calls add a ApiRequest instance
     * ApiRequest instance contains all the helper method for munch-core ApiService
     */
    public void before(Request request, Response response) {
        // See JsonCall.get & JsonCall.put to understand how it works

        ApiRequest apiRequest = apiAuthenticator.authenticate(request);
        request.attribute(ApiRequest.class.getName(), apiRequest);
    }

    /**
     * Special handle AuthenticationException of tracking purpose
     *
     * @param call      json call
     * @param exception to handle
     */
    @Override
    protected void handleException(JsonCall call, StructuredException exception) {
        if (exception instanceof AuthenticationException) {
            ApiRequest request = call.get(ApiRequest.class);
            logger.warn("AuthenticationException User: {}", request.optionalUserId().orElse(null));
        }
        super.handleException(call, exception);
    }
}
