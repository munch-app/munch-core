package munch.api.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import munch.api.data.Place;
import munch.restful.client.RestfulClient;

import javax.annotation.Nullable;
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

    @Inject
    public PlaceClient(@Named("services") Config config) {
        super(config.getString("places.url"));
    }

    /**
     * @param key key of place
     * @return single Place
     */
    public Place get(String key) {
        return doGet("/places/{key}")
                .path("key", key)
                .hasMetaCodes(200, 404)
                .asDataObject(Place.class);
    }

    /**
     * @param keys list of keys for place
     * @return list of Place
     */
    public List<Place> get(List<String> keys) {
        return doPost("/places/get")
                .body(keys)
                .hasMetaCodes(200)
                .asDataList(Place.class);
    }

    /**
     * @param from     list from size
     * @param size     size per list
     * @param geometry geometry to filter
     * @param query    text query
     * @param filters  Place filters
     * @return List of Place results
     */
    public List<Place> search(int from, int size, JsonNode geometry, String query, Place.Filters filters) {
        ObjectNode node = mapper.createObjectNode();
        node.put("from", from);
        node.put("size", size);
        node.set("geometry", geometry);
        node.put("query", query);
        node.set("filters", mapper.valueToTree(filters));
        return search(node);
    }

    /**
     * @param node node
     * @return List of Place results
     * @see PlaceClient#search(int, int, JsonNode, String, Place.Filters)
     */
    public List<Place> search(JsonNode node) {
        return doPost("/places/search")
                .body(node)
                .hasMetaCodes(200)
                .asDataList(Place.class);
    }

    /**
     * Suggest place data based on name
     *
     * @param size  size per list
     * @param query text query
     * @return List of Place results
     */
    public List<Place> suggest(int size, String query, @Nullable String latLng) {
        ObjectNode node = mapper.createObjectNode();
        node.put("size", size);
        node.put("query", query);
        node.put("latLng", latLng);

        return doPost("/places/suggest")
                .body(node)
                .hasMetaCodes(200)
                .asDataList(Place.class);
    }
}
