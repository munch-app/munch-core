package munch.api.search.plugin.location;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchLocationAreaCard;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.plugin.SearchCardPlugin;
import munch.data.client.AreaClient;
import munch.data.client.ElasticClient;
import munch.data.elastic.ElasticUtils;
import munch.data.location.Area;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    private final AreaClient areaClient;
    private final LoadingCache<String, Optional<SearchLocationAreaCard>> loadingCache;

    @Inject
    public SearchLocationAreaPlugin(ElasticClient elasticClient, AreaClient areaClient) {
        this.elasticClient = elasticClient;
        this.areaClient = areaClient;
        this.loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(13, TimeUnit.HOURS)
                .build(CacheLoader.from(this::loadCard));
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Location)) return null;

        List<SearchLocationAreaCard> cards = loadCards().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return of(-1, cards);
    }

    private List<Optional<SearchLocationAreaCard>> loadCards() {
        return List.of(
                // 1. Orchard
                loadingCache.getUnchecked("2e3c2ba4-24ff-495d-a4e5-330b70564d9a"),
                // 2. Somerset
                loadingCache.getUnchecked("d725fb16-32a2-48bb-8533-2ccc29de6612"),
                // 3. Dhoby Ghaut
                loadingCache.getUnchecked("736ca726-5909-40dd-8dca-2bd1928ce9ee"),
                // 4. City Hall
                loadingCache.getUnchecked("2fd1f4e1-c141-439e-9d2d-6c2ad2fdd3a3"),
                // 5. Bugis
                loadingCache.getUnchecked("c998d7ce-e516-47a7-b88e-b328f517036b"),
                // 6. Chinatown
                loadingCache.getUnchecked("5dc8a0f6-6e63-4f4d-b7f0-9a2993c119ec"),
                // 7. Little India
                loadingCache.getUnchecked("4d42b573-3d74-4711-867b-13a471a27c92"),
                // 8. Tanjong Pagar
                loadingCache.getUnchecked("c31060da-1431-49c2-8ceb-6e2022622dc3"),
                // 9. Katong
                loadingCache.getUnchecked("588b842c-a26b-4541-bef8-f9fe7b2bbe18"),
                // 10. Jurong East
                loadingCache.getUnchecked("4eecfcc0-d0a0-492b-9be9-fd2d47316884")
        );
    }

    private Optional<SearchLocationAreaCard> loadCard(String areaId) {
        Area area = areaClient.get(areaId);
        if (area == null) return Optional.empty();

        List<Place> places = searchPlace(area, 6);
        if (places.isEmpty()) return Optional.empty();

        places.forEach(place -> place.getAreas().clear());

        area.setNames(null);
        area.setCounts(null);
        area.setHours(null);
        area.setImages(null);

        return Optional.of(new SearchLocationAreaCard(area, places));
    }

    private List<Place> searchPlace(Area area, int size) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", size);

        ObjectNode bool = JsonUtils.createObjectNode();
        root.set("query", JsonUtils.createObjectNode().set("bool", bool));

        ArrayNode filter = bool.putArray("filter");
        filter.add(ElasticUtils.filterTerm("dataType", "Place"));
        filter.add(ElasticUtils.filterTerm("status.type", "open"));

        // Polygon points is required.
        if (area.getLocation().getPolygon() == null) return List.of();
        filter.add(ElasticQueryUtils.filterPolygon(area.getLocation().getPolygon().getPoints()));

        root.set("sort", ElasticUtils.sortField("taste.importance", "desc"));
        return elasticClient.searchHitsHits(root);
    }
}
