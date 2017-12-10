package munch.api;

import com.fasterxml.jackson.databind.JsonNode;
import munch.restful.client.RestfulClient;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 8:46 AM
 * Project: munch-core
 */
@Singleton
public final class HealthService implements JsonService {

    private final HealthCheck[] healthChecks;

    @Inject
    public HealthService() {
        this.healthChecks = new HealthCheck[]{
        };
    }

    @Override
    public void route() {
        GET("/health/check", this::check);
    }

    private JsonNode check(JsonCall call) {
        for (HealthCheck healthCheck : healthChecks) {
            healthCheck.check();
        }
        return Meta200;
    }

    private class HealthCheck extends RestfulClient {
        private HealthCheck(String url) {
            super(url);
        }

        protected void check() {
            doGet("/health/check").hasCode(200);
        }
    }
}
