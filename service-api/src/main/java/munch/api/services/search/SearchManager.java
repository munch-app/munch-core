package munch.api.services.search;

import munch.api.services.search.cards.*;
import munch.data.clients.PlaceClient;
import munch.data.structure.Location;
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
    private static final Location LOCATION_SINGAPORE = createSingapore();
    private static final SearchNoResultCard CARD_NO_RESULT = new SearchNoResultCard();
    private static final SearchNoLocationCard CARD_NO_LOCATION = new SearchNoLocationCard();

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
        List<Place> places = placeClient.search(query);
        List<SearchCard> cards = cardParser.parseCards(places);
        injectCards(cards, query);
        return cards;
    }

    private void injectCards(List<SearchCard> cards, SearchQuery query) {
        if (cards.isEmpty() && query.getFrom() == 0) {
            if (query.getLatLng() != null &&
                    query.getLocation() == null ||
                    !"singapore".equals(query.getLocation().getId())) {
                // Inject Search Anywhere Card
                query.setLocation(LOCATION_SINGAPORE);
                cards.add(0, new SearchNoResultAnywhereCard(query));
            } else {
                // Inject No Result Card
                cards.add(0, CARD_NO_RESULT);
            }
        }

        // Inject No Location Card
        if (query.getLatLng() == null && query.getFrom() == 0) {
            cards.add(0, CARD_NO_LOCATION);
        }
    }

    private static Location createSingapore() {
        Location location = new Location();
        location.setId("singapore");
        location.setName("Singapore");
        location.setCountry("singapore");
        location.setCity("singapore");
        location.setLatLng("1.290270, 103.851959");
        location.setPoints(List.of("1.26675774823,103.603134155", "1.32442122318,103.617553711", "1.38963424766,103.653259277", "1.41434608581,103.666305542", "1.42944763543,103.671798706", "1.43905766081,103.682785034", "1.44386265833,103.695831299", "1.45896401284,103.720550537", "1.45827758983,103.737716675", "1.44935407163,103.754196167", "1.45004049736,103.760375977", "1.47887018872,103.803634644", "1.4754381021,103.826980591", "1.45827758983,103.86680603", "1.43219336108,103.892211914", "1.4287612035,103.897018433", "1.42670190649,103.915557861", "1.43219336108,103.934783936", "1.42189687297,103.960189819", "1.42464260763,103.985595703", "1.42121043879,104.000701904", "1.43974408965,104.02130127", "1.44592193988,104.043960571", "1.42464260763,104.087219238", "1.39718511473,104.094772339", "1.35737118164,104.081039429", "1.29009788407,104.127044678", "1.277741368,104.127044678", "1.25371463932,103.982162476", "1.17545464492,103.812561035", "1.13014521522,103.736343384", "1.19055762617,103.653945923", "1.1960495989,103.565368652", "1.26675774823,103.603134155"));
        return location;
    }
}
