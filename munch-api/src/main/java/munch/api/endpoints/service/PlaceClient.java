package munch.api.endpoints.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;
import munch.struct.places.Place;

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
public class PlaceClient extends RestfulClient {

    /**
     * Look at data service package to api service settings
     *
     * @param config config to load data.url
     */
    @Inject
    public PlaceClient(@Named("services") Config config) {
        super(config.getString("places.url"));
    }

    public Place get(String key) {
        return doGet("/places/{key}")
                .path("key", key)
                .hasMetaCodes(200, 404)
                .asDataObject(Place.class);
    }

    public List<Place> get(List<String> keys) {
        return doGet("/places/batch/get")
                .body(keys)
                .hasMetaCodes(200)
                .asDataList(Place.class);
    }

    public List<Place> search(int from, int size, JsonNode geometry) {
        ObjectNode query = mapper.createObjectNode();
        query.put("from", from);
        query.put("size", size);
        query.set("geometry", geometry);
        return doPost("/places/search")
                .body(query)
                .hasMetaCodes(200)
                .asDataList(Place.class);
    }
}
