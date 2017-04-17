package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import munch.struct.Place;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    /**
     * @param node search node
     * @return root node
     */
    JsonNode search(JsonNode node) throws IOException {
        HttpEntity jsonEntity = new NStringEntity(mapper.writeValueAsString(node), ContentType.APPLICATION_JSON);
        Response response = client.performRequest("POST", "/munch/place/_search",
                Collections.emptyMap(), jsonEntity);

        HttpEntity entity = response.getEntity();
        InputStream input = entity.getContent();
        try {
            return mapper.readTree(input);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    @Override
    public List<Place> query(JsonNode node) throws IOException {
        JsonNode root = search(node);
        List<Place> places = new ArrayList<>();
        for (JsonNode hit : root.path("hits").path("hits")) {
            Place place = new Place();
            place.setId(hit.path("_id").asText());
            place.setName(hit.path("_source").path("name").asText());
            places.add(place);
        }
        return places;
    }
}
