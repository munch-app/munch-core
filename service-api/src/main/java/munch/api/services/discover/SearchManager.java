package munch.api.services.discover;

import munch.api.services.discover.cards.CardParser;
import munch.api.services.discover.cards.SearchCard;
import munch.data.clients.PlaceClient;
import munch.data.clients.SearchClient;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 18/10/2017
 * Time: 1:39 AM
 * Project: munch-core
 */
@Singleton
public final class SearchManager {

    private final PlaceClient placeClient;
    private final SearchClient searchClient;
    private final CardParser cardParser;

    private final InjectedCardManager injectedCardManager;

    private final FixedRandomSorter placeSorter = new FixedRandomSorter(Duration.ofHours(24 + 18));

    @Inject
    public SearchManager(PlaceClient placeClient, SearchClient searchClient, CardParser cardParser, InjectedCardManager injectedCardManager) {
        this.placeClient = placeClient;
        this.searchClient = searchClient;
        this.cardParser = cardParser;
        this.injectedCardManager = injectedCardManager;
    }

    /**
     * @param query  query to search
     * @param userId nullable user id
     * @return List of SearchCard
     */
    public List<SearchCard> search(SearchQuery query, @Nullable String userId) {
        query.setRadius(resolveRadius(query));
        List<Place> places = placeClient.getSearchClient().search(query);
        List<SearchCard> cards = cardParser.parseCards(sort(places));
        injectedCardManager.inject(cards, query, userId);
        return cards;
    }

    private List<Place> sort(List<Place> places) {
        List<Place> topSortList = new ArrayList<>();
        List<Place> botSortList = new ArrayList<>();
        List<Place> noImageList = new ArrayList<>();

        for (Place place : places) {
            if (place.getImages() == null || place.getImages().isEmpty()) {
                noImageList.add(place);
            } else if (place.getRanking() > 1015) {
                topSortList.add(place);
            } else {
                botSortList.add(place);
            }
        }
        placeSorter.sort(topSortList);
        placeSorter.sort(botSortList);

        noImageList.addAll(0, botSortList);
        noImageList.addAll(0, topSortList);
        return noImageList;
    }

    public Map<String, Object> suggest(Map<String, Integer> types, String text, @Nullable String latLng, SearchQuery prevQuery) {
        Map<String, Object> resultMap = new HashMap<>();
        searchClient.multiSearch(types, text.toLowerCase(), latLng).forEach((type, results) -> {
            if (!results.isEmpty()) {
                resultMap.put(type, results);
            }
        });

        return resultMap;
    }

    public static double resolveRadius(SearchQuery query) {
        Double radius = query.getRadius();
        if (radius == null) return 800; // Default radius
        if (radius > 3000) return 3000; // Max radius for nearby search
        return radius;
    }
}
