package com.munch.struct.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.struct.Location;
import com.munch.struct.Place;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
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
     * @param client  injected rest client
     * @param mapping mapping validator
     * @throws RuntimeException if ElasticSearchMapping validation failed
     */
    @Inject
    public ElasticSearchDatabase(RestClient client, ElasticSearchMapping mapping) {
        this.client = client;
        try {
            if (!mapping.validate())
                throw new RuntimeException("ElasticSearchMapping for /munch/place index validation failed.");
        } catch (IOException e) {
            throw new RuntimeException("ElasticSearchMapping failed", e);
        }
    }

    /**
     * @param response response to check if successful
     * @throws RuntimeException if response code is higher
     */
    private void isSuccessful(Response response) throws RuntimeException {
        int code = response.getStatusLine().getStatusCode();
        if (code > 299) {
            throw new RuntimeException("Response code is higher than 299, code: " + code);
        }
    }

    @Override
    public void put(Place place) throws Exception {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", place.getName());
        node.put("phone", place.getPhone());
        node.put("website", place.getWebsite());
        node.put("description", place.getDescription());

        node.set("location", locationNode(place.getLocation()));

        String json = mapper.writeValueAsString(node);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);

        Response response = client.performRequest(
                "PUT",
                "/munch/place/" + place.getId(),
                Collections.emptyMap(),
                entity);
        isSuccessful(response);
    }

    private ObjectNode locationNode(Location location) {
        // location node
        ObjectNode node = mapper.createObjectNode();
        node.put("address", location.getAddress());
        node.put("city", location.getCity());
        node.put("country", location.getCountry());
        node.put("postal", location.getPostal());

        // https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-shape.html
        // location.geo Node, only if lat, lng exist
        if (location.getLng() != null) {
            ObjectNode geo = mapper.createObjectNode();
            geo.put("type", "Point");

            // location.geo.coordinates Node
            ArrayNode coordinates = mapper.createArrayNode();
            coordinates.add(location.getLng());
            coordinates.add(location.getLat());
            geo.set("coordinates", coordinates);

            node.set("geo", geo);
        }

        return node;
    }

    @Override
    public void put(List<Place> places) throws Exception {
        if (places.isEmpty()) return;
        for (Place place : places) {
            put(place);
        }
    }

    @Override
    public void delete(String key) throws Exception {
        try {
            Response response = client.performRequest(
                    "DELETE",
                    "/munch/place/" + key,
                    Collections.emptyMap());
            isSuccessful(response);
        } catch (ResponseException responseException) {
            int code = responseException.getResponse().getStatusLine().getStatusCode();
            if (code != 404) {
                throw responseException;
            }
        }
    }

    @Override
    public void delete(List<String> keys) throws Exception {
        if (keys.isEmpty()) return;
        for (String key : keys) {
            delete(key);
        }
    }
}
