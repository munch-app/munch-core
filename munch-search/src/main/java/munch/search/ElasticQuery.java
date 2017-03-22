package munch.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import munch.struct.Place;
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
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public class ElasticQuery implements SearchQuery {

    private final RestClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public ElasticQuery(RestClient client) {
        this.client = client;
    }

    @Override
    public List<Place> query(JsonNode node) throws IOException {
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
