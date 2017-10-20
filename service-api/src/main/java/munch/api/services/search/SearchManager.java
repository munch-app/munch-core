package munch.api.services.search;

import munch.api.services.search.cards.CardParser;
import munch.api.services.search.cards.SearchCard;
import munch.api.services.search.cards.SearchNoLocationCard;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
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

    @Inject
    public SearchManager(PlaceClient placeClient, CardParser cardParser) {
        this.placeClient = placeClient;
        this.cardParser = cardParser;
    }

    /**
     * @param query query to search
     * @return List of SearchCard
     */
    public List<SearchCard> search(SearchQuery query) {
        List<SearchCard> cards = new ArrayList<>();
        if (query.getLatLng() == null && query.getFrom() == 0) {
            cards.add(new SearchNoLocationCard());
        }

        List<Place> places = placeClient.search(query);
        cards.addAll(cardParser.parseCards(places));

        return cards;
    }

    // TODO No Location Search
    // Inject cards based on query
}
