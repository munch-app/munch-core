package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;

/**
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 11:22 PM
 * Project: munch-core
 */
@Singleton
public class ElasticMapping {

    private final RestClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public static final String TYPE = "geo_shape";
    public static final String TREE = "quadtree";
    public static final String PRECISION = "1.0m";

    @Inject
    public ElasticMapping(RestClient client) {
        this.client = client;
    }

    /**
     * Validate that /munch/place has geo mapping
     *
     * @return true if passed
     * @throws IOException io exception
     */
    public boolean validate() throws IOException {
        JsonNode index;
        try {
            index = getIndex();
        } catch (ResponseException e) {
            // Check if 404 index_not_found_exception
            JsonNode error = mapper.readTree(e.getResponse().getEntity().getContent());
            if (error.has("error")) {
                String type = error.path("error").path("root_cause").get(0).path("type").asText();
                if (type.equalsIgnoreCase("index_not_found_exception")) {
                    createIndex();
                }
            }
            index = getIndex();
        }


        // Validate index
        JsonNode geoProps = index.path("munch").path("mappings")
                .path("place").path("properties")
                .path("location").path("properties")
                .path("geo");

        return geoProps.path("type").asText().equalsIgnoreCase(TYPE) &&
                geoProps.path("tree").asText().equalsIgnoreCase(TREE) &&
                geoProps.path("precision").asText().equalsIgnoreCase(PRECISION);

    }

    /**
     * @return index of /munch
     * @throws IOException io exception
     */
    private JsonNode getIndex() throws IOException {
        Response response = client.performRequest(
                "GET",
                "/munch",
                Collections.emptyMap());

        return mapper.readTree(response.getEntity().getContent());
    }

    /**
     * Create index for /munch/place
     *
     * @throws IOException io exception
     */
    private void createIndex() throws IOException {
        String json = "{" +
                "  \"mappings\": {" +
                "    \"place\": {" +
                "      \"properties\": {" +
                "        \"location.geo\": {" +
                "          \"type\": \"" + TYPE + "\"," +
                "          \"tree\": \"" + TREE + "\"," +
                "          \"precision\": \"" + PRECISION + "\"" +
                "        }" +
                "      }" +
                "    }" +
                "  }" +
                "}";

        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        client.performRequest(
                "PUT",
                "/munch",
                Collections.emptyMap(),
                entity);
    }
}
