package munch.api.services.search;

import com.rits.cloning.Cloner;
import munch.api.clients.SearchClient;
import munch.api.services.search.cards.SearchCard;
import munch.api.services.search.cards.SearchCollection;
import munch.api.services.search.cards.SearchPlaceCard;
import munch.data.Place;
import munch.data.SearchQuery;
import munch.data.SearchResult;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 10:21 PM
 * Project: munch-core
 */
public abstract class Curator {
    protected static final Cloner cloner = new Cloner();

    @Inject
    protected SearchClient searchClient;

    /**
     * @param query  mandatory query in search bar
     * @return true if current curator can return results
     */
    public abstract boolean match(SearchQuery query);

    /**
     * @param query  mandatory query in search bar
     * @return Curated List of PlaceCollection
     */
    public abstract List<SearchCollection> curate(SearchQuery query);

    /**
     * @param query query to clone
     * @return deep cloned query
     */
    public static SearchQuery clone(SearchQuery query) {
        return cloner.deepClone(query);
    }

    /**
     * Only search results that is place is parsed now
     *
     * @param results search results
     * @return List of Parsed SearchCard
     */
    static List<SearchCard> parseCards(List<SearchResult> results) {
        List<SearchCard> cards = new ArrayList<>();
        for (SearchResult result : results) {
            // Since now there is only one card type, simple method to address the problem
            SearchCard card = parseCard(result);
            if (card != null) cards.add(card);
        }
        return cards;
    }

    /**
     * @param result parsable result
     * @return SearchCard
     */
    @Nullable
    static SearchCard parseCard(SearchResult result) {
        if (result instanceof Place) {
            SearchPlaceCard card = new SearchPlaceCard();
            card.setPlace((Place) result);
            return card;
        }
        return null;
    }
}
