package munch.api.services.search;

import munch.api.services.search.cards.CardParser;
import munch.api.services.search.cards.SearchCard;
import munch.api.services.search.inject.SearchCardInjector;
import munch.data.clients.PlaceClient;
import munch.data.clients.SearchClient;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
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

    private final SearchCardInjector searchCardInjector;

    private final FixedRandomSorter placeSorter = new FixedRandomSorter(Duration.ofHours(24 + 18));

    @Inject
    public SearchManager(PlaceClient placeClient, SearchClient searchClient, CardParser cardParser, SearchCardInjector searchCardInjector) {
        this.placeClient = placeClient;
        this.searchClient = searchClient;
        this.cardParser = cardParser;
        this.searchCardInjector = searchCardInjector;
    }

    /**
     * @param query  query to search
     * @param userId nullable user id
     * @return List of SearchCard
     */
    public List<SearchCard> search(SearchQuery query, @Nullable String userId) {
        query.setRadius(resolveRadius(query));
        List<Place> places = placeClient.getSearchClient().search(query);
        List<SearchCard> cards = cardParser.parseCards(sort(places, query), userId);
        searchCardInjector.inject(cards, query, userId);
        return cards;
    }

    private List<Place> sort(List<Place> places, SearchQuery query) {
        placeSorter.sort(places, query);
        return places;
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
        if (radius == null) return 750; // Default radius
        if (radius > 2500) return 2500; // Max radius for nearby search
        return radius;
    }
}
