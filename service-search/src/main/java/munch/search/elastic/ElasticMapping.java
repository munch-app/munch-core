package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

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

    private final JestClient client;
    private final ObjectMapper mapper;

    @Inject
    public ElasticMapping(JestClient client, ObjectMapper mapper) {
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
        JestResult result = client.execute(new GetMapping.Builder()
                .addIndex("munch")
                .build());
        JsonNode node = mapper.readTree(result.getJsonString());
        String type = node.path("error").path("type").asText(null);
        if (StringUtils.equalsIgnoreCase(type, "index_not_found_exception")) {
            return null;
        }
        return node;
    }

    /**
     * Create index for /munch/place
     *
     * @throws IOException io exception
     */
    public void createIndex() throws IOException {
        URL url = Resources.getResource("mappings.json");
        String json = Resources.toString(url, Charset.forName("UTF-8"));
        client.execute(new PutMapping.Builder("munch", null, json).build());
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
