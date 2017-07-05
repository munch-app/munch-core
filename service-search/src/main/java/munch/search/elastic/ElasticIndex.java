package munch.search.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.search.data.Place;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

import java.util.Collections;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:27 PM
 * Project: munch-core
 */
@Singleton
public class ElasticIndex {

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
    public ElasticIndex(RestClient client, ObjectMapper mapper) {
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
        ObjectNode node = mapper.createObjectNode();
        node.put("name", place.getName());
        node.put("phone", place.getPhone());
        node.put("website", place.getWebsite());
        node.put("description", place.getDescription());

        // Tags array node, Index as lower case
        ArrayNode tagsNode = mapper.createArrayNode();
        for (String tag : place.getTags()) tagsNode.add(tag);
        node.set("tags", tagsNode);

        // Location Node
        node.set("location", locationNode(place.getLocation()));
        // Price Node
        if (place.getPrice() != null) {
            node.set("price", priceNode(place.getPrice()));
        }

        // Hours array node
        ArrayNode hoursNode = mapper.createArrayNode();
        for (Place.Hour hour : place.getHours()) hoursNode.add(hourNode(hour));
        node.set("hours", hoursNode);

        // Suggest Field
        ArrayNode suggest = mapper.createArrayNode();
        suggest.add(place.getName());
        node.set("suggest", suggest);

        String json = mapper.writeValueAsString(node);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);

        Response response = client.performRequest("PUT",
                "/munch/place/" + place.getId(), Collections.emptyMap(), entity);
        isSuccessful(response);
    }

    public void put(List<Place> places) throws Exception {
        if (places.isEmpty()) return;
        for (Place place : places) {
            put(place);
        }
    }

    public void delete(String key) throws Exception {
        try {
            Response response = client.performRequest("DELETE",
                    "/munch/place/" + key, Collections.emptyMap());
            isSuccessful(response);
        } catch (ResponseException responseException) {
            int code = responseException.getResponse().getStatusLine().getStatusCode();
            if (code != 404) throw responseException;
        }
    }

    public void delete(List<String> keys) throws Exception {
        if (keys.isEmpty()) return;
        for (String key : keys) {
            delete(key);
        }
    }

    private ObjectNode locationNode(Place.Location location) {
        // location node
        ObjectNode node = mapper.createObjectNode();
        node.put("address", location.getAddress());
        node.put("city", location.getCity());
        node.put("country", location.getCountry());
        node.put("postal", location.getPostal());

        // https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-point.html
        node.put("latLng", location.getLatLng());
        return node;
    }

    private ObjectNode priceNode(Place.Price price) {
        ObjectNode node = mapper.createObjectNode();
        node.put("lowest", price.getLowest());
        node.put("highest", price.getHighest());
        return node;
    }

    private ObjectNode hourNode(Place.Hour hour) {
        ObjectNode node = mapper.createObjectNode();
        node.put("day", hour.getDay().name());
        node.put("open", hour.getOpen());
        node.put("close", hour.getClose());
        return node;
    }
}
