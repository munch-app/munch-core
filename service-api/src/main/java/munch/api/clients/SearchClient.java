package munch.api.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.data.*;
import munch.restful.client.RestfulClient;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.JsonException;

import javax.annotation.Nullable;
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
    public SearchClient(Config config) {
        super(config.getString("services.search.url"));
    }


    /**
     * @param query query object
     * @return List of SearchResult
     * @see SearchQuery
     */
    public List<SearchResult> search(SearchQuery query) {
        return deserialize(searchRaw(query));
    }

    /**
     * Suggest place data based on name
     *
     * @param size  size per list
     * @param query text query
     * @return List of SearchResult
     */
    public List<SearchResult> suggest(int size, String query, @Nullable String latLng) {
        return deserialize(suggestRaw(size, query, latLng));
    }

    /**
     * @param query query object
     * @return List of Place results
     * @see SearchQuery
     */
    public JsonNode searchRaw(SearchQuery query) {
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
    public JsonNode suggestRaw(int size, String query, @Nullable String latLng) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("size", size);
        node.put("query", query);
        node.put("latLng", latLng);

        return doPost("/suggest")
                .body(node)
                .asDataNode();
    }

    /**
     * @param dataNode data node
     * @return List of SearchResult
     * @throws JsonException instead of JsonProcessingException
     */
    private List<SearchResult> deserialize(JsonNode dataNode) throws JsonException {
        return JsonUtils.toList(dataNode, node ->
                parse(node.path("dataType").asText(), node));
    }

    /**
     * @param type class name (usually)
     * @param node node with fields
     * @return single search result
     * @throws JsonException instead of JsonProcessingException
     */
    private SearchResult parse(String type, JsonNode node) throws JsonException {
        try {
            switch (type) {
                case "Location":
                    return objectMapper.treeToValue(node, Location.class);
                case "Place":
                    return objectMapper.treeToValue(node, Place.class);
                case "Tag":
                    return objectMapper.treeToValue(node, Tag.class);
                default:
                    return null;
            }
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
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
