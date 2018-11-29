package munch.api.search.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.cards.SearchAreaClusterListCard;
import munch.data.client.ElasticClient;
import munch.data.elastic.ElasticUtils;
import munch.data.location.Area;
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
public final class SearchAreaClusterListLoader implements SearchCardPlugin {
    private final ObjectMapper mapper = JsonUtils.objectMapper;
    private final ElasticClient elasticClient;

    @Inject
    public SearchAreaClusterListLoader(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return List.of();
        if (request.isComplex()) return List.of();

        // Only for Nearby & Anywhere
        if (!(request.getRequest().isNearby() || request.getRequest().isAnywhere())) return List.of();

        String latLng = request.getLatLngContext();
        if (latLng == null) return List.of();

        List<Area> areas = getNearbyAreas(latLng, 6);
        if (areas.isEmpty()) return List.of();

        return of(-1_000, new SearchAreaClusterListCard(areas));
    }

    private List<Area> getNearbyAreas(String latLng, int size) {
        ObjectNode root = mapper.createObjectNode();
        root.put("from", 0);
        root.put("size", size);

        ObjectNode boolQuery = mapper.createObjectNode();
        boolQuery.set("filter", mapper.createArrayNode()
                .add(ElasticUtils.filterTerm("dataType", "Area"))
                .add(ElasticUtils.filterTerm("type", "Cluster"))
                .add(ElasticUtils.filterRange("counts.total", "gte", 4))
        );
        root.putObject("query").set("bool", boolQuery);

        root.set("sort", mapper.createArrayNode()
                .add(ElasticUtils.sortDistance(latLng))
        );

        return elasticClient.searchHitsHits(root);
    }
}
