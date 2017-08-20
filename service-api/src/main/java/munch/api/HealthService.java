package munch.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
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

    private final HealthCheckClient checkClient;

    @Inject
    public HealthService(Config config) {
        this.checkClient = new HealthCheckClient(config);
    }

    @Override
    public void route() {
        GET("/health/check", this::check);
    }

    private JsonNode check(JsonCall call) {
        checkClient.checkData();
        checkClient.checkSearch();
        return Meta200;
    }

    private class HealthCheckClient extends RestfulClient {
        private final String dataUrl;
        private final String searchUrl;

        private HealthCheckClient(Config config) {
            super("");
            this.dataUrl = config.getString("services.data.url");
            this.searchUrl = config.getString("services.search.url");
        }

        private void checkData() {
            doGet(dataUrl).hasCode(200);
        }

        private void checkSearch() {
            doGet(searchUrl).hasCode(200);
        }
    }
}
