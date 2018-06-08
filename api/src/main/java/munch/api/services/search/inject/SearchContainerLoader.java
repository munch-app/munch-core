package munch.api.services.search.inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.services.search.cards.SearchContainersCard;
import munch.data.clients.ContainerClient;
import munch.data.elastic.query.BoolQuery;
import munch.data.elastic.query.SortQuery;
import munch.data.structure.Container;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 5/2/2018
 * Time: 12:42 AM
 * Project: munch-core
 */
@Singleton
public final class SearchContainerLoader implements SearchCardInjector.Loader {
    private final ObjectMapper mapper = JsonUtils.objectMapper;
    private final ContainerClient containerClient;

    @Inject
    public SearchContainerLoader(ContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @Override
    public List<Position> load(Request request) {
        if (request.isComplex()) return List.of();
        if (request.hasContainer()) return List.of();

        String latLng = request.getLatLngContext();
        if (latLng == null) return List.of();

        List<Container> containers = getNearbyContainer(latLng, 6);
        if (containers.isEmpty()) return List.of();

        return of(-1_000, new SearchContainersCard(containers));
    }

    private List<Container> getNearbyContainer(String latLng, int size) {
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
}
