package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.search.data.Location;
import munch.search.data.Place;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

import java.util.Collections;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:27 PM
 * Project: munch-core
 */
@Singleton
public class ElasticDatabase {
    private static final Map<String, String> PARAMS = Collections.emptyMap();

    private final RestClient client;
    private final ObjectMapper mapper;

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
     *
     * @param client injected rest client
     * @param mapper jackson json mapper
     * @throws RuntimeException if ElasticSearchMapping validation failed
     */
    @Inject
    public ElasticDatabase(RestClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * This method also consume response entity
     *
     * @param response response to check if successful
     * @throws RuntimeException if response code is higher
     */
    private void isSuccessful(Response response) throws RuntimeException {
        int code = response.getStatusLine().getStatusCode();
        EntityUtils.consumeQuietly(response.getEntity());
        if (code > 299) {
            throw new RuntimeException("Response code is higher than 299, code: " + code);
        }
    }

    /**
     * Index a place by putting it into elastic search
     *
     * @param place place to index
     * @throws Exception any exception
     */
    public void put(Place place) throws Exception {
        ObjectNode node = mapper.valueToTree(place);
        node.put("createdDate", place.getCreatedDate().getTime());
        node.put("updatedDate", place.getUpdatedDate().getTime());

        // Suggest Field
        ArrayNode suggest = mapper.createArrayNode();
        suggest.add(place.getName());
        node.set("suggest", suggest);

        String json = mapper.writeValueAsString(node);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);

        String endpoint = "/munch/place/" + place.getId();
        Response response = client.performRequest("PUT", endpoint, PARAMS, entity);
        isSuccessful(response);
    }

    /**
     * Index a location by putting it into elastic search
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-shape.html
     *
     * @param location location to index
     * @throws Exception any exception
     */
    public void put(Location location) throws Exception {
        ObjectNode node = mapper.valueToTree(location);
        node.put("updatedDate", location.getUpdatedDate().getTime());

        // TODO Polygon indexing
        // TODO Custom Deserialization

        // Suggest Field
        ArrayNode suggest = mapper.createArrayNode();
        suggest.add(location.getName());
        node.set("suggest", suggest);

        String json = mapper.writeValueAsString(node);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);

        String endpoint = "/munch/location/" + location.getId();
        Response response = client.performRequest("PUT", endpoint, PARAMS, entity);
        isSuccessful(response);
    }

    /**
     * @param type data type to delete before
     * @param key  key of data type
     * @throws Exception exception for deletion
     */
    public void delete(String type, String key) throws Exception {
        try {
            String endpoint = "/munch/" + type + "/" + key;
            Response response = client.performRequest("DELETE", endpoint);
            isSuccessful(response);
        } catch (ResponseException responseException) {
            int code = responseException.getResponse().getStatusLine().getStatusCode();
            if (code != 404) throw responseException;
        }
    }

    /**
     * @param type        data type to delete before
     * @param updatedDate updated date in millis
     * @throws Exception exception for deletion
     */
    public void deleteBefore(String type, long updatedDate) throws Exception {
        JsonNode cycleNode = mapper.createObjectNode().put("lt", updatedDate);
        JsonNode rangeNode = mapper.createObjectNode().set("updatedDate", cycleNode);
        JsonNode queryNode = mapper.createObjectNode().set("range", rangeNode);
        JsonNode rootNode = mapper.createObjectNode().set("query", queryNode);

        String json = mapper.writeValueAsString(rootNode);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        String endpoint = "/munch/" + type + "_delete_by_query?conflicts=proceed";
        Response response = client.performRequest("POST", endpoint, PARAMS, entity);
        isSuccessful(response);
    }
}
