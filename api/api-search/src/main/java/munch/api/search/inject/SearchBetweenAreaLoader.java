package munch.api.search.inject;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edit.utils.LatLngUtils;
import munch.api.search.cards.SearchBetweenAreaCard;
import munch.api.search.cards.SearchNoResultCard;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.data.client.ElasticClient;
import munch.data.location.Area;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 25/9/18
 * Time: 2:12 PM
 * Project: munch-core
 */
@Singleton
public final class SearchBetweenAreaLoader implements SearchCardInjector.Loader {
    private static final SearchNoResultCard CARD_NO_RESULT = new SearchNoResultCard();

    private static final Logger logger = LoggerFactory.getLogger(SearchBetweenAreaLoader.class);
    private final ElasticClient elasticClient;

    @Inject
    public SearchBetweenAreaLoader(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    @Override
    public List<Position> load(Request request) {
        if (!request.isBetween()) return List.of();

        List<Area> areas = findAreas(request);
        if (areas.isEmpty()) return noResult(request);

        Pair<List<Place>, Area> pair = getPlaces(request, areas);
        if (pair == null) return noResult(request);


        List<Place> places = pair.getLeft();
        Area area = pair.getRight();

        SearchBetweenAreaCard card = new SearchBetweenAreaCard();
        card.setIndex(request.getPage());
        card.setArea(area);
        card.setPlaces(places);

        request.getAreas().add(area);
        return of(-1, card);
    }

    private List<Position> noResult(Request request) {
        if (request.getPage() > 1) return List.of();
        return of(-1, CARD_NO_RESULT);
    }

    private List<Area> findAreas(Request request) {
        String centroid = getCentroid(request.getRequest().getPoints());

        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 50);

        ArrayNode filterArray = JsonUtils.createArrayNode();
        filterArray.add(ElasticQueryUtils.filterTerm("dataType", "Area"));
        filterArray.add(ElasticQueryUtils.filterRange("counts.total", "gte", 45));

        ObjectNode bool = JsonUtils.createObjectNode();
        bool.set("must", ElasticQueryUtils.mustMatchAll());
        bool.set("filter", filterArray);
        root.set("query", JsonUtils.createObjectNode().set("bool", bool));


        ArrayNode sortArray = JsonUtils.createArrayNode();
        sortArray.add(ElasticSortUtils.sortDistance(centroid));
        root.set("sort", sortArray);
        return elasticClient.searchHitsHits(root);
    }

    private Pair<List<Place>, Area> getPlaces(Request request, List<Area> areas) {
        for (Area area : areas) {
            if (request.getAreas().stream().anyMatch(a -> a.getAreaId().equals(area.getAreaId()))) continue;

            ObjectNode root = JsonUtils.createObjectNode();
            root.put("from", 0);
            root.put("size", 30);
            root.set("query", ElasticQueryUtils.make(
                    request.getRequest(),
                    ElasticQueryUtils.filterRange("taste.group", "gte", 1),
                    ElasticQueryUtils.filterArea(area)
            ));
            root.set("sort", ElasticSortUtils.make(request.getRequest()));

            List<Place> places = elasticClient.searchHitsHits(root);
            // Only use places with more than 10 results
            if (places.size() < 10) continue;

            return Pair.of(places, area);
        }
        return null;
    }

    public static String getCentroid(List<SearchQuery.Filter.Location.Point> points) {
        double centroidLat = 0, centroidLng = 0;

        for (SearchQuery.Filter.Location.Point point : points) {
            LatLngUtils.LatLng latLng = LatLngUtils.parse(point.getLatLng());
            centroidLat += latLng.getLat();
            centroidLng += latLng.getLng();
        }

        return centroidLat / points.size() + "," + centroidLng / points.size();
    }
}
