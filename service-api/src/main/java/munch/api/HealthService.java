package munch.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.typesafe.config.Config;
import munch.restful.client.ExceptionParser;
import munch.restful.client.RestfulClient;
import munch.restful.core.exception.UnknownException;
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
    public HealthService(Config config) {
        this.healthChecks = new HealthCheck[]{
                new HealthCheck(config.getString("services.data.url")),
                new HealthCheck(config.getString("services.search.url")),
                new NominatimClient(config.getString("services.nominatim.url"))
        };
    }

    @Override
    public void route() {
        GET("/health/check", this::check);
    }

    private JsonNode check(JsonCall call) throws UnirestException {
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

    private final class NominatimClient extends HealthCheck {
        private NominatimClient(String url) {
            super(url);
        }

        @Override
        protected void check() {
            try {
                int statusCode = Unirest.get(url + "/search?format=json").asString().getStatus();
                if (statusCode != 200) throw new RuntimeException("Nominatim not ready");
            } catch (UnirestException e) {
                ExceptionParser.parse(e);
                throw new UnknownException(e);
            }
        }
    }
}
