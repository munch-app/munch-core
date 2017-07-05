package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Collections;
import java.util.Map;

/**
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
        if (index == null) {
            // Index don't exist; hence create
            create();
            sleep();
            index = getIndex();
        }

        if (!validate(index)) throw new RuntimeException("Index validation failed.");
    }

    /**
     * Validate location.latLng type is geo_point
     * Validate suggest is completion
     *
     * @return true = passed
     */
    private boolean validate(JsonNode node) {
        JsonNode properties = node.path("munch")
                .path("mappings").path("place").path("properties");

        // Validate geo type is geo_point
        JsonNode geo = properties.path("location").path("properties").path("latLng");
        if (!geo.path("type").asText().equals("geo_point")) return false;

        // Validate suggest field
        JsonNode suggest = properties.get("suggest");
        if (!suggest.path("type").asText().equals("completion")) return false;
        if (!suggest.has("contexts")) return false;
        if (suggest.path("contexts").size() != 1) return false;

        JsonNode latLng = suggest.path("contexts").get(0);
        if (!latLng.path("name").asText().equals("latLng")) return false;
        return latLng.path("path").asText().equals("location.latLng");
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
    public void create() throws IOException {
        String json = "{" +
                "    \"mappings\": {" +
                "        \"place\" : {" +
                "            \"properties\" : {" +
                "                \"suggest\" : {" +
                "                    \"type\" : \"completion\"," +
                "                    \"contexts\": [" +
                "                        { " +
                "                            \"name\": \"latLng\"," +
                "                            \"type\": \"geo\"," +
                "                            \"precision\": 5," +
                "                            \"path\": \"location.latLng\"" +
                "                        }" +
                "                    ]" +
                "                }," +
                "                \"location.latLng\": {" +
                "                    \"type\": \"geo_point\"" +
                "                }" +
                "            }" +
                "        }" +
                "    }" +
                "}";
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        client.performRequest("PUT", "/munch", PARAMS, entity);
    }

    /**
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
