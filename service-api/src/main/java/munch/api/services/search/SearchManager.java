package munch.api.services.search;

import munch.api.services.search.cards.CardParser;
import munch.api.services.search.cards.SearchCard;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/10/2017
 * Time: 1:39 AM
 * Project: munch-core
 */
@Singleton
public final class SearchManager {
    private final PlaceClient placeClient;
    private final CardParser cardParser;

    private final InjectedCardManager injectedCardManager;

    @Inject
    public SearchManager(PlaceClient placeClient, CardParser cardParser, InjectedCardManager injectedCardManager) {
        this.placeClient = placeClient;
        this.cardParser = cardParser;
        this.injectedCardManager = injectedCardManager;
    }

    /**
     * @param query query to search
     * @return List of SearchCard
     */
    public List<SearchCard> search(SearchQuery query) {
        query.setRadius(resolveRadius(query));
        List<Place> places = placeClient.search(query);
        List<SearchCard> cards = cardParser.parseCards(places);
        injectedCardManager.inject(cards, query);
        return cards;
    }

    private static double resolveRadius(SearchQuery query) {
        Double radius = query.getRadius();
        if (radius == null) return 800; // Default radius
        if (radius > 3000) return 3000; // Max radius for nearby search
        return radius;
    }
}
