package munch.api.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.api.data.Location;
import munch.api.data.SearchQuery;
import munch.restful.client.RestfulClient;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/7/2017
 * Time: 6:29 PM
 * Project: munch-core
 */
@Singleton
public class SearchClient extends RestfulClient {

    @Inject
    public SearchClient(@Named("services") Config config) {
        super(config.getString("search.url"));
    }

    /**
     * @param query query object
     * @return List of Place results
     * @see SearchQuery
     */
    public JsonNode search(SearchQuery query) {
        return doPost("/search")
                .body(query)
                .asDataNode();
    }

    /**
     * Suggest place data based on name
     *
     * @param size  size per list
     * @param query text query
     * @return List of Place results
     */
    public JsonNode suggest(int size, String query, @Nullable String latLng) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("size", size);
        node.put("query", query);
        node.put("latLng", latLng);

        return doPost("/suggest")
                .body(node)
                .asDataNode();
    }

    /**
     * @param latLng latLng in "lat,lng"
     * @return Location or null if cannot find
     */
    public Location reverseLocation(String latLng) {
        return doGet("/locations/reverse")
                .queryString("latLng", latLng)
                .asDataObject(Location.class);
    }

    /**
     * @param text text
     * @param size size of location to suggest
     * @return list of Location
     */
    public List<Location> suggestLocation(String text, int size) {
        return doGet("/locations/suggest")
                .queryString("text", text)
                .queryString("size", size)
                .asDataList(Location.class);
    }
}
