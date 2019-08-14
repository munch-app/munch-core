package app.munch.api;

import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.restful.server.JsonService;

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
public final class ApiHealthService implements JsonService {

    private final Set<ApiHealthCheck> healthChecks;

    @Inject
    public ApiHealthService(Set<ApiHealthCheck> healthChecks) {
        this.healthChecks = healthChecks;
    }

    @Override
    public void route() {
        GET("/health/check", this::check);
    }

    private JsonResult check(JsonCall call) throws Exception {
        for (ApiHealthCheck healthCheck : healthChecks) {
            healthCheck.check();
        }
        return JsonResult.ok();
    }
}
