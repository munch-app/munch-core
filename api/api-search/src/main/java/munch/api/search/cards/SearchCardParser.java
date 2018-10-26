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
                .map(SearchCardParser::parse)
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
            cards.add(new SearchHeaderCard(neighbourhood));
            map.get(neighbourhood).forEach(place -> {
                cards.add(parse(place));
            });
        }
        return cards;
    }

    private static SearchPlaceCard parse(Place place) {
        SearchPlaceCard card = new SearchPlaceCard();
        card.setPlace(place);
        return card;
    }
}
