package munch.api.search.cards;

import munch.api.search.SearchRequest;
import munch.data.place.Place;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 16/9/2017
 * Time: 5:38 PM
 * Project: munch-core
 */
@Singleton
public final class SearchCardParser {

    /**
     * Only search results that is place is parsed now
     *
     * @param places search results
     * @return List of Parsed SearchCard
     */
    public List<SearchCard> parse(List<Place> places, SearchRequest request) {
        if (request.isBetween()) {
            return sortedNeighbourhood(places);
        }

        return places.stream()
                .map(SearchPlaceCard::new)
                .collect(Collectors.toList());
    }

    private static List<SearchCard> sortedNeighbourhood(List<Place> places) {
        List<String> ordered = new ArrayList<>();
        Map<String, List<Place>> map = new HashMap<>();

        for (Place place : places) {
            String neighbourhood = place.getLocation().getNeighbourhood();
            if (!map.containsKey(neighbourhood)) {
                map.put(neighbourhood, new ArrayList<>());
                ordered.add(neighbourhood);
            }
            map.get(neighbourhood).add(place);
        }

        List<SearchCard> cards = new ArrayList<>();
        for (String neighbourhood : ordered) {
            SearchHeaderCard card = new SearchHeaderCard(neighbourhood);
            card.setSticky(true);
            cards.add(card);

            map.get(neighbourhood).forEach(place -> {
                cards.add(new SearchPlaceCard(place));
            });
        }
        return cards;
    }
}
