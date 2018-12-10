package munch.api.search.plugin.location;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchLocationAreaCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.data.client.ElasticClient;
import munch.data.elastic.ElasticUtils;
import munch.data.location.Area;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 5:15 PM
 * Project: munch-core
 */
@Singleton
public final class SearchLocationAreaPlugin implements SearchCardPlugin {

    private final ElasticClient elasticClient;

    @Inject
    public SearchLocationAreaPlugin(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isFeature(SearchQuery.Feature.Location)) return null;
        if (request.getRequest().getPage() > 10) return null;

        int size = 5;
        int from = request.getRequest().getPage() * size;

        List<SearchLocationAreaCard> cards = searchArea(from, size).stream()
                .map(area -> {
                    List<Place> places = searchPlace(area, 20);
                    return new SearchLocationAreaCard(area, places);
                })
                .collect(Collectors.toList());

        return of(0, cards);
    }

    private List<Place> searchPlace(Area area, int size) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", size);

        ObjectNode bool = JsonUtils.createObjectNode();

        ArrayNode filter = bool.putArray("filter");
        filter.add(ElasticUtils.filterTerm("dataType", "Place"));
        filter.add(ElasticUtils.filterTerm("areas.areaId", area.getAreaId()));
        bool.set("filter", filter);

        root.set("query", JsonUtils.createObjectNode().set("bool", bool));
        root.set("sort", ElasticUtils.sortField("taste.importance", "desc"));
        return elasticClient.searchHitsHits(root);
    }

    private List<Area> searchArea(int from, int size) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", from);
        root.put("size", size);

        ObjectNode bool = JsonUtils.createObjectNode();
        ArrayNode filter = bool.putArray("filter");
        filter.add(ElasticUtils.filterTerm("dataType", "Area"));
        filter.add(ElasticUtils.filterTerms("type", Set.of(
                Area.Type.Cluster.name(),
                Area.Type.Region.name()
        )));

        bool.set("filter", filter);
        root.set("query", JsonUtils.createObjectNode().set("bool", bool));
        root.set("sort", ElasticUtils.sortField("counts.total", "desc"));
        return elasticClient.searchHitsHits(root);
    }
}
