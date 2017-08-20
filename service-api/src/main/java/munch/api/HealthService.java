package munch.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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

    private final HealthCheckClient[] checkClients;
    private final NominatimCheckClient nominatimCheckClient;

    @Inject
    public HealthService(Config config) {
        this.checkClients = new HealthCheckClient[]{
                new HealthCheckClient(config.getString("services.data.url")),
                new HealthCheckClient(config.getString("services.search.url"))
        };
        this.nominatimCheckClient = new NominatimCheckClient(config.getString("services.nominatim.url"));
    }

    @Override
    public void route() {
        GET("/health/check", this::check);
    }

    private JsonNode check(JsonCall call) throws UnirestException {
        for (HealthCheckClient checkClient : checkClients) {
            checkClient.check();
        }
        nominatimCheckClient.check();
        return Meta200;
    }

    private class HealthCheckClient extends RestfulClient {
        private HealthCheckClient(String url) {
            super(url);
        }

        private void check() {
            doGet("/health/check").hasCode(200);
        }
    }

    private class NominatimCheckClient {

        private final String url;

        private NominatimCheckClient(String url) {
            this.url = url;
        }

        private void check() throws UnirestException {
            int statusCode = Unirest.get(url + "/search?format=json").asString().getStatus();
            if (statusCode != 200) throw new RuntimeException("Nominatim not ready");
        }
    }
}
