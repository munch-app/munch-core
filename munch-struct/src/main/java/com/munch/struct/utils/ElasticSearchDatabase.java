package com.munch.struct.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.struct.Place;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
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
public class ElasticSearchDatabase implements SearchDatabase {

    private final RestClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
     *
     * @param client injected rest client
     */
    @Inject
    public ElasticSearchDatabase(RestClient client) {
        this.client = client;
    }

    /**
     * @param response response to check if successful
     * @throws RuntimeException if response code is higher
     */
    private void isSuccessful(Response response) throws RuntimeException {
        int code = response.getStatusLine().getStatusCode();
        if (code < 300) {
            throw new RuntimeException("Response code is higher than 300, code: " + code);
        }
    }

    @Override
    public void put(Place place) throws Exception {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", place.getName());
        node.put("phone", place.getPhone());
        node.put("website", place.getWebsite());
        node.put("description", place.getDescription());

        ObjectNode location = mapper.createObjectNode();
        location.put("address", place.getLocation().getAddress());
        location.put("city", place.getLocation().getCity());
        location.put("country", place.getLocation().getCountry());
        location.put("postal", place.getLocation().getPostal());
        location.put("lat", place.getLocation().getLat());
        location.put("lng", place.getLocation().getLng());
        node.set("location", location);

        String json = mapper.writeValueAsString(node);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);

        Response response = client.performRequest(
                "PUT",
                "/munch/place/" + place.getId(),
                Collections.emptyMap(),
                entity);
        isSuccessful(response);
    }

    @Override
    public void put(List<Place> places) throws Exception {
        for (Place place : places) {
            put(place);
        }
    }

    @Override
    public void delete(String key) throws Exception {
        Response response = client.performRequest(
                "DELETE",
                "/munch/place/" + key,
                Collections.emptyMap());
        isSuccessful(response);
    }

    @Override
    public void delete(List<String> keys) throws Exception {
        for (String key : keys) {
            delete(key);
        }
    }
}
