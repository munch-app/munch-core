package munch.api.search.between;

import com.fasterxml.jackson.databind.node.ObjectNode;
import edit.utils.LatLngUtils;
import munch.api.search.SearchRequest;
import munch.api.search.cards.SearchBetweenAreaCard;
import munch.api.search.cards.SearchBetweenNoResultCard;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.api.search.inject.SearchCardInjector;
import munch.data.client.ElasticClient;
import munch.data.location.Area;
import munch.data.place.Place;
import munch.location.LocationClient;
import munch.restful.core.JsonUtils;
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
    private static final SearchBetweenNoResultCard CARD_NO_RESULT = new SearchBetweenNoResultCard();

    private static final Logger logger = LoggerFactory.getLogger(SearchBetweenAreaLoader.class);

    private final ElasticClient elasticClient;
    private final BetweenAreaGenerator areaGenerator;
    private final LocationClient locationClient;

    @Inject
    public SearchBetweenAreaLoader(ElasticClient elasticClient, BetweenAreaGenerator areaGenerator, LocationClient locationClient) {
        this.elasticClient = elasticClient;
        this.areaGenerator = areaGenerator;
        this.locationClient = locationClient;
    }

    @Override
    public List<Position> load(Request request) {
        if (!request.isBetween()) return List.of();

        if (request.getPage() == 1) {
            List<Area> areas = areaGenerator.generate(request);
            for (Area area : areas) {
                // TODO Remove Later
                logger.info("Area: {}", area);
            }
            if (areas.isEmpty()) return of(-1, CARD_NO_RESULT);
            request.getAreas().addAll(areas);
        }

        if (request.getAreas().size() <= request.getPage()) return List.of();

        Area area = request.getAreas().get(request.getPage());
        List<Place> places = getPlaces(request.getRequest(), area);

        // Update Name of area
        if (area.getName() == null) {
            String latLng = area.getLocation().getLatLng();
            area.setName(getName(latLng));
            area.getLocation().setStreet(getStreet(latLng));
        }

        SearchBetweenAreaCard card = new SearchBetweenAreaCard();
        card.setIndex(request.getPage());
        card.setCount(request.getRequest().getPoints().size());
        card.setArea(area);
        card.setPlaces(places);
        return of(-1, card);
    }

    private List<Place> getPlaces(SearchRequest request, Area area) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 30);
        root.set("query", ElasticQueryUtils.make(
                request,
                ElasticQueryUtils.filterRange("taste.group", "gte", 1),
                ElasticQueryUtils.filterArea(area)
        ));
        root.set("sort", ElasticSortUtils.make(request));

        return elasticClient.searchHitsHits(root);
    }

    private String getName(String centroid) {
        LatLngUtils.LatLng latLng = LatLngUtils.parse(centroid);
        return locationClient.getNeighbourhood(latLng.getLat(), latLng.getLng());
    }

    private String getStreet(String centroid) {
        LatLngUtils.LatLng latLng = LatLngUtils.parse(centroid);
        return locationClient.getStreet(latLng.getLat(), latLng.getLng());
    }
}
