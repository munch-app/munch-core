package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public final class ElasticClient {
    private static final Map<String, String> PARAMS = Collections.emptyMap();

    private final RestClient client;
    private final ObjectMapper mapper;

    @Inject
    public ElasticClient(RestClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters-completion.html#querying
     *
     * @param query  query string
     * @param latLng nullable latLng
     * @param size   size of suggestion of place
     * @return options array nodes containing the results
     */
    public JsonNode suggest(String type, String query, @Nullable String latLng, int size) throws IOException {
        ObjectNode completion = mapper.createObjectNode()
                .put("field", "suggest")
                .put("size", size);
        ObjectNode contexts = completion.putObject("contexts");

        // Context: LatLng
        if (StringUtils.isNotBlank(latLng)) {
            String[] lls = latLng.split(",");

            contexts.putObject("latLng")
                    .put("lat", Double.parseDouble(lls[0].trim()))
                    .put("lon", Double.parseDouble(lls[1].trim()));
        }

        // Context: Type
        if (type != null) {
            contexts.put("type", WordUtils.capitalize(type));
        }

        ObjectNode root = mapper.createObjectNode();
        root.putObject("suggest")
                .putObject("suggestions")
                .put("prefix", query)
                .set("completion", completion);

        // Query, parse and return options array node
        return postSearch(null, root)
                .path("suggest")
                .path("suggestions")
                .get(0)
                .path("options");
    }

    /**
     * @param type      type to focus
     * @param from      page from
     * @param size      page size
     * @param boolQuery bool query node
     * @return JsonNode
     * @throws IOException exception
     */
    public JsonNode postBoolSearch(String type, int from, int size, JsonNode boolQuery) throws IOException {
        return postBoolSearch(type, from, size, boolQuery, null);
    }


    /**
     * @param type      type to focus
     * @param from      page from
     * @param size      page size
     * @param boolQuery bool query node
     * @param sort      sort nodes
     * @return JsonNode
     * @throws IOException exception
     */
    public JsonNode postBoolSearch(String type, int from, int size, JsonNode boolQuery, @Nullable JsonNode sort) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("from", from);
        root.put("size", size);
        root.putObject("query").set("bool", boolQuery);
        if (sort != null) root.set("sort", sort);

        return postSearch(type, root);
    }

    /**
     * @param type type to focus
     * @param node search node
     * @return root node
     */
    public JsonNode postSearch(String type, JsonNode node) throws IOException {
        String endpoint = "/munch/" + (type != null ? type + "/" : "") + "_search";
        HttpEntity jsonEntity = new NStringEntity(mapper.writeValueAsString(node), ContentType.APPLICATION_JSON);
        Response response = client.performRequest("POST", endpoint, PARAMS, jsonEntity);
        HttpEntity entity = response.getEntity();

        try {
            InputStream input = entity.getContent();
            return mapper.readTree(input);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }
}
