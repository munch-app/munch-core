package app.munch.api;

import com.typesafe.config.ConfigFactory;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.TransportException;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportServer;
import dev.fuxing.transport.service.TransportService;
import munch.restful.core.exception.AuthenticationException;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.RestfulService;
import org.apache.tika.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public final class ApiServer extends TransportServer {
    private static final Logger logger = LoggerFactory.getLogger(ApiServer.class);
    private static final Set<String> ORIGINS = Set.of(
            "http://localhost:3000", "https://munch.app", "https://www.munch.app", "https://staging.munch.app"
    );

    private final ApiAuthenticator authenticator;
    private final ApiHealthService healthService;

    private final Set<String> adminAccountIds;

    @Deprecated
    Set<RestfulService> deprecatedServices;

    @Inject
    public ApiServer(Set<TransportService> services, ApiAuthenticator authenticator, ApiHealthService healthService, Set<RestfulService> deprecatedServices) {
        super(services);
        this.authenticator = authenticator;
        this.healthService = healthService;
        this.deprecatedServices = deprecatedServices;
        setDebug(false);

        this.adminAccountIds = Set.copyOf(
                ConfigFactory.load().getStringList("api.admin.accountIds")
        );
    }

    @Override
    protected void setupRouters() {
        // Added /* because beginning of path has version number
        PATH("/*", () -> {
            BEFORE("/*", this::defaultBefore);
            AFTER_AFTER("/*", this::defaultAfterAfter);
            BEFORE("/admin/*", this::adminBefore);
            super.setupRouters();

            deprecatedServices.forEach(restfulService -> {
                restfulService.start();
                logger.info("Started RestfulService: {}", restfulService.getClass().getSimpleName());
            });
        });

        healthService.start();
        logger.info("Started Service: ApiHealthService");
    }

    /**
     * Before all calls add a ApiRequest instance
     * ApiRequest instance contains all the helper method for munch-core ApiService
     */
    public void defaultBefore(TransportContext ctx) throws AuthenticationException {
        ApiRequest request = authenticator.authenticate(ctx);
        ctx.put(request);
    }

    private void defaultAfterAfter(TransportContext ctx) {
        String origin = ctx.getHeader("Origin");
        if (origin != null && ORIGINS.contains(origin)) {
            ctx.response().header("Access-Control-Allow-Headers", "Origin,Authorization,Local-Lat-Lng,Local-Zone-Id");
            ctx.response().header("Access-Control-Allow-Origin", origin);
        }
    }

    /**
     * For admin path, only pre approved accountId can access
     */
    private void adminBefore(TransportContext ctx) throws UnauthorizedException {
        ApiRequest request = ctx.get(ApiRequest.class);
        @NotNull String accountId = request.getAccountId();

        if (adminAccountIds.contains(accountId)) return;
        throw new UnauthorizedException();
    }

    @Override
    protected boolean mapException(Exception exception) throws TransportException {
        if (exception instanceof StructuredException) {
            // Deprecation Support
            throw new StructuredTransportException((StructuredException) exception);
        }

        return super.mapException(exception);
    }

    static class StructuredTransportException extends TransportException {
        StructuredTransportException(StructuredException err) {
            super(err.getCode(), err.getType(), err.getMessage(), ExceptionUtils.getStackTrace(err));
        }
    }

    /**
     * Special handle AuthenticationException of tracking purpose
     *
     * @param context   transport context
     * @param exception to handle
     */
    @Override
    protected void handleException(TransportContext context, TransportException exception) {
        if (exception instanceof UnauthorizedException || exception instanceof ForbiddenException) {
            ApiRequest request = context.get(ApiRequest.class);
            logger.warn("AuthException: userId: {}", request.accountId().orElse(null));
        }
        super.handleException(context, exception);
    }
}
