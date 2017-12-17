package munch.api.services.search;

import munch.api.services.search.cards.*;
import munch.data.clients.ContainerClient;
import munch.data.structure.Container;
import munch.data.structure.Location;
import munch.data.structure.SearchQuery;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/12/2017
 * Time: 4:43 AM
 * Project: munch-core
 */
@Singleton
public final class InjectedCardManager {
    private static final Location LOCATION_SINGAPORE = createSingapore();
    private static final SearchNoResultCard CARD_NO_RESULT = new SearchNoResultCard();
    private static final SearchNoLocationCard CARD_NO_LOCATION = new SearchNoLocationCard();

    private final ContainerClient containerClient;

    @Inject
    public InjectedCardManager(ContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public void inject(List<SearchCard> cards, SearchQuery query) {
        // Leading injected cards
        if (query.getFrom() == 0) {
            cards.addAll(0, getLeadingInjectedCards(cards.isEmpty(), query));
        }
    }

    private List<SearchCard> getLeadingInjectedCards(boolean isEmpty, SearchQuery query) {
        List<SearchCard> injectedList = new ArrayList<>();

        // if noLatLng = NoLocationCard
        if (query.getLatLng() == null) {
            injectedList.add(CARD_NO_LOCATION);
        }

        if (isContainerAllowed(query)) {
            List<Container> containers = containerClient.search(query.getLatLng(), 800, 15);
            if (!containers.isEmpty()) {
                injectedList.add(new SearchContainersCard(containers));
            }
        }

        // others = NoResultCards
        if (isEmpty) {
            if (isLocationAnywhere(query)) {
                // Inject No Result Card
                injectedList.add(CARD_NO_RESULT);
            } else {
                if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());

                // Inject No Result Location Card with location name
                SearchNoResultLocationCard card = new SearchNoResultLocationCard();
                card.setLocationName(getLocationName(query));
                query.getFilter().setLocation(LOCATION_SINGAPORE);
                query.getFilter().setContainers(List.of());
                card.setSearchQuery(query);

                injectedList.add(card);
            }
        }

        return injectedList;
    }

    private static String getLocationName(SearchQuery query) {
        if (query.getFilter().getContainers() != null) {
            if (!query.getFilter().getContainers().isEmpty()) {
                return query.getFilter().getContainers().get(0).getName();
            }
        }
        if (query.getFilter().getLocation() != null) {
            return query.getFilter().getLocation().getName();
        }
        return null;
    }

    private static boolean isContainerAllowed(SearchQuery query) {
        if (query.getLatLng() == null) return false;
        if (StringUtils.isNotBlank(query.getQuery())) return false;

        if (query.getFilter() != null) {
            SearchQuery.Filter filter = query.getFilter();
            if (filter.getContainers() != null) {
                if (!filter.getContainers().isEmpty()) return false;
            }

            if (filter.getLocation() != null) return false;
        }

        if (query.getSort() != null) {
            if (SearchQuery.Sort.TYPE_MUNCH_RANK.equals(query.getSort().getType())) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param query search query
     * @return is location is anywhere
     */
    private static boolean isLocationAnywhere(SearchQuery query) {
        if (query.getLatLng() == null) {
            // No current lat lng given hence
            if (query.getFilter() == null) return true;
            if (query.getFilter().getLocation() == null) return true;
            return "singapore".equals(query.getFilter().getLocation().getId());
        } else {
            if (query.getFilter() == null) return false;
            if (query.getFilter().getLocation() == null) return false;
            return "singapore".equals(query.getFilter().getLocation().getId());
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
