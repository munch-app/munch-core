package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.munch.struct.Place;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 20/3/2017
 * Time: 11:25 PM
 * Project: munch-core
 */
@Singleton
public class SearchService {

    private final RestClient client;
    private final ObjectMapper mapper;

    @Inject
    public SearchService(RestClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    /**
     * @param node search query
     * @return List of Place
     */
    public List<Place> search(JsonNode node) throws IOException {
        String json = mapper.writeValueAsString(node);
        HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
        // TODO Elastic search query
        Response response = client.performRequest(
                "PUT",
                "/munch/place/",
                Collections.emptyMap(),
                entity);
        return null;
    }

}
