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
    // https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html

    private final RestClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public ElasticSearchDatabase(RestClient client) {
        this.client = client;
    }

    @Override
    public void put(Place place) throws Exception {
        ObjectNode node = mapper.createObjectNode();
        // TODO node values

        String json = mapper.writeValueAsString(node);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);

        Response response = client.performRequest(
                "PUT",
                "/munch/place/" + place.getId(),
                Collections.emptyMap(),
                entity);
        int code = response.getStatusLine().getStatusCode();
        if (code < 300) {
            throw new RuntimeException("Response code is higher than 300, code: " + code);
        }
    }

    @Override
    public void put(List<Place> places) throws Exception {
        for (Place place : places) {
            put(place);
        }
    }

    @Override
    public void delete(String key) throws Exception {
        // TODO delete response function
    }

    @Override
    public void delete(List<String> keys) throws Exception {
        for (String key : keys) {
            delete(key);
        }
    }
}
