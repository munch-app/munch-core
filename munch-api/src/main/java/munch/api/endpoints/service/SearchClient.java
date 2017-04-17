package munch.api.endpoints.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.struct.place.Place;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 5:06 PM
 * Project: munch-core
 */
@Singleton
public class SearchClient extends RestfulClient {

    private final ObjectMapper mapper;

    /**
     * Look at data service package to api service settings
     *
     * @param config config to load data.url
     * @param mapper injected object mapper
     */
    @Inject
    public SearchClient(@Named("services") Config config, ObjectMapper mapper) {
        super(config.getString("search.url"));
        this.mapper = mapper;
    }

    public List<Place> search(int from, int size, JsonNode geometry) {
        ObjectNode query = mapper.createObjectNode();
        query.put("from", from);
        query.put("size", size);
        query.set("geometry", geometry);
        return doPost("/places/search")
                .body(query)
                .asDataList(Place.class);
    }
}
