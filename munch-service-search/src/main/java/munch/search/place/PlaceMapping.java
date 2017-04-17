package munch.search.place;

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
public class PlaceMapping {

    public static final String TYPE = "geo_shape";
    public static final String TREE = "quadtree";
    public static final String PRECISION = "10.0m";

    private final RestClient client;
    private final Validator validator;
    private final ObjectMapper mapper;

    /**
     * Universal result
     */
    public enum Result {
        Success,
        NoIndexError,
        GeoIndexError,
        UnknownError
    }

    @Inject
    public PlaceMapping(RestClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
        this.validator = new Validator();
    }

    /**
     * Validate that /munch/place has geo mapping
     *
     * @return Result
     * @throws IOException io exception
     * @see Result
     */
    public Result validate() throws IOException {
        try {
            // Check if index exist
            JsonNode root = getIndex();

            // Validate geo node
            if (!validator.isGeo(root)) return Result.GeoIndexError;

            // Else success
            return Result.Success;
        } catch (ResponseException e) {
            // Check if 404 index_not_found_exception
            JsonNode error = mapper.readTree(e.getResponse().getEntity().getContent());
            if (error.has("error")) {
                String type = error.path("error").path("root_cause").get(0).path("type").asText();
                if (type.equalsIgnoreCase("index_not_found_exception")) {
                    return Result.NoIndexError;
                }
            }
        }

        return Result.UnknownError;
    }

    /**
     * Create index for /munch/place
     *
     * @throws IOException io exception
     */
    public void createIndex() throws IOException {
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
        client.performRequest("PUT", "/munch", Collections.emptyMap(), entity);
    }

    /**
     * @return index of /munch
     * @throws IOException io exception
     */
    private JsonNode getIndex() throws IOException {
        Response response = client.performRequest("GET", "/munch", Collections.emptyMap());
        return mapper.readTree(response.getEntity().getContent());
    }

    /**
     * Validator of index
     */
    private class Validator {
        /**
         * @param rootNode root node
         * @return if geo node is valid
         */
        private boolean isGeo(JsonNode rootNode) {
            JsonNode geoProps = rootNode.path("munch").path("mappings")
                    .path("place").path("properties")
                    .path("location").path("properties")
                    .path("geo");

            return geoProps.path("type").asText().equalsIgnoreCase(TYPE) &&
                    geoProps.path("tree").asText().equalsIgnoreCase(TREE) &&
                    geoProps.path("precision").asText().equalsIgnoreCase(PRECISION);
        }
    }
}
