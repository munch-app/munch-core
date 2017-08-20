package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by: Fuxing
 * Date: 20/8/2017
 * Time: 8:53 AM
 * Project: munch-core
 */
@Singleton
public final class HealthService implements JsonService {

    private final RestClient client;

    @Inject
    public HealthService(RestClient client) {
        this.client = client;
    }

    @Override
    public void route() {
        GET("/health/check", this::check);
    }

    private JsonNode check(JsonCall call) throws IOException {
        Response response = client.performRequest("GET", "/_cluster/health");
        HttpEntity entity = response.getEntity();

        try {
            InputStream input = entity.getContent();
            String status = objectMapper.readTree(input).path("status").asText();
            if (StringUtils.equalsAnyIgnoreCase(status, "yellow", "green")) {
                return Meta200;
            } else {
                return nodes(503, status);
            }
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }
}
