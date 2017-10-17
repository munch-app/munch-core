package munch.api.services.search.cards;

import munch.data.structure.Place;
import munch.data.structure.SearchResult;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 16/9/2017
 * Time: 5:38 PM
 * Project: munch-core
 */
@Singleton
public final class CardParser {

    /**
     * Only search results that is place is parsed now
     *
     * @param results search results
     * @return List of Parsed SearchCard
     */
    public List<SearchCard> parseCards(List<? extends SearchResult> results) {
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
    public SearchCard parseCard(SearchResult result) {
        if (result instanceof Place) {
            return parse((Place) result);
        }
        return null;
    }

    private SearchCard parse(Place place) {
        SearchPlaceCard card = new SearchPlaceCard();
        card.setPlaceId(place.getId());
        card.setImages(place.getImages());
        card.setName(place.getName());

        card.setTags(place.getTag().getExplicits());
        card.setLocation(place.getLocation());
        card.setHours(place.getHours());
        return card;
    }
}
