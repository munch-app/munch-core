package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
 * For validation of Mappings
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 11:22 PM
 * Project: munch-core
 */
@Singleton
public final class ElasticMapping {
    private static final Logger logger = LoggerFactory.getLogger(ElasticMapping.class);
    private static final Map<String, String> PARAMS = Collections.emptyMap();

    private final RestClient client;
    private final ObjectMapper mapper;

    @Inject
    public ElasticMapping(RestClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * Does 2 things
     * 1. Try to create mappings if don't exist
     * 2. Validate mappings
     *
     * @throws RuntimeException if failed to create or validate
     */
    public void tryCreate() throws RuntimeException, IOException {
        sleep();
        logger.info("Validating Index for endpoint /munch");
        JsonNode index = getIndex();

        // Index don't exist; hence create and revalidate
        if (index == null) {
            createIndex();
            sleep();
            index = getIndex();
        }

        if (!validate(index)) throw new RuntimeException("Index validation failed.");
    }

    /**
     * Major migrate of elastic search mapping should be done by migrate
     * to a whole new elastic search version with different endpoint
     *
     * @return true = passed
     */
    private boolean validate(JsonNode node) {
        JsonNode mappings = node.path("munch").path("mappings");

        // Validate has these types
        if (!mappings.path("place").has("properties")) return false;
        return mappings.path("location").has("properties");
    }

    /**
     * Index of corpus
     *
     * @return null if not found
     */
    @Nullable
    private JsonNode getIndex() throws IOException {
        try {
            Response response = client.performRequest("GET", "/munch");
            return mapper.readTree(response.getEntity().getContent());
        } catch (ResponseException e) {
            // Check if 404 index_not_found_exception
            JsonNode error = mapper.readTree(e.getResponse().getEntity().getContent());
            String type = error.path("error").path("type").asText(null);
            if (StringUtils.equalsIgnoreCase(type, "index_not_found_exception")) {
                return null;
            }

            logger.error("/munch error json: {}", error);
            throw e;
        }
    }

    /**
     * Create index for /munch/place
     *
     * @throws IOException io exception
     */
    public void createIndex() throws IOException {
        URL url = Resources.getResource("mappings.json");
        String json = Resources.toString(url, Charset.forName("UTF-8"));
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        client.performRequest("PUT", "/munch", PARAMS, entity);
    }

    /**
     * Wait for es to apply change
     * sleep for 2 seconds
     */
    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
