package munch.api.search.cards;

import munch.api.search.SearchRequest;
import munch.data.place.Place;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 16/9/2017
 * Time: 5:38 PM
 * Project: munch-core
 */
@Singleton
public class SearchCardParser {

    /**
     * Only search results that is place is parsed now
     *
     * @param places search results
     * @return List of Parsed SearchCard
     */
    public List<SearchCard> parseCards(List<Place> places, SearchRequest request) {
        return places.stream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private SearchPlaceCard parse(Place place) {
        if (place.getImages().isEmpty()) {
            SearchPlaceCard.Small small = new SearchPlaceCard.Small();
            small.setPlace(place);
            return small;
        } else {
            SearchPlaceCard card = new SearchPlaceCard();
            card.setPlace(place);
            return card;
        }
    }
}
