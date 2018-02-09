package munch.api.services.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.collections.RecentPlace;
import munch.collections.RecentPlaceClient;
import munch.data.clients.ContainerClient;
import munch.data.clients.PlaceClient;
import munch.data.elastic.query.BoolQuery;
import munch.data.elastic.query.SortQuery;
import munch.data.structure.Container;
import munch.data.structure.Place;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 5/2/2018
 * Time: 12:42 AM
 * Project: munch-core
 */
@Singleton
public final class MunchAssistManager {
    private final ObjectMapper mapper = JsonUtils.objectMapper;
    private final PlaceClient placeClient;
    private final RecentPlaceClient recentClient;
    private final ContainerClient containerClient;

    @Inject
    public MunchAssistManager(PlaceClient placeClient, RecentPlaceClient recentClient, ContainerClient containerClient) {
        this.placeClient = placeClient;
        this.recentClient = recentClient;
        this.containerClient = containerClient;
    }

    public List<Container> getNearbyContainer(String latLng, int size) {
        ObjectNode root = mapper.createObjectNode();
        root.put("from", 0);
        root.put("size", size);

        ObjectNode boolQuery = mapper.createObjectNode();
        ArrayNode filterArray = mapper.createArrayNode();
        filterArray.add(BoolQuery.filterTerm("dataType", "Container"));
        filterArray.add(BoolQuery.filterRange("count", "gte", 4));

        boolQuery.set("filter", filterArray);
        root.putObject("query").set("bool", boolQuery);

        ArrayNode sortArray = mapper.createArrayNode();
        sortArray.add(SortQuery.sortDistance(latLng));
        root.set("sort", sortArray);
        return containerClient.search(root);
    }

    public List<Place> getRecentPlaces(String userId, int size) {
        Objects.requireNonNull(userId);

        List<RecentPlace> list = recentClient.list(userId, null, size);
        if (list.size() < 4) return List.of();
        return placeClient.batchGetMap(list, RecentPlace::getPlaceId, (recentPlace, place) -> place);
    }

    public List<Place> getNewestPlaces(String latLng, double metres, int size) {
        ObjectNode root = mapper.createObjectNode();
        root.put("from", 0);
        root.put("size", size);

        // Bool
        ObjectNode boolQuery = mapper.createObjectNode();
        ArrayNode filterArray = mapper.createArrayNode();
        filterArray.add(BoolQuery.filterTerm("dataType", "Place"));
        filterArray.add(BoolQuery.filterDistance(latLng, metres));
        // filterArray.add(BoolQuery.filterRange("ranking", "gt", 1));

        // Filter Array
        boolQuery.set("filter", filterArray);
        root.putObject("query").set("bool", boolQuery);

        // Sorting
        ArrayNode sortArray = mapper.createArrayNode();
        sortArray.add(SortQuery.sortField("createdDate", "desc"));
        root.set("sort", sortArray);

        return placeClient.getSearchClient().search(root);
    }
}
