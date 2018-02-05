package munch.api.services.search;

import munch.api.services.search.cards.*;
import munch.data.structure.Container;
import munch.data.structure.Location;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final MunchAssistManager assistManager;

    @Inject
    public InjectedCardManager(MunchAssistManager assistManager) {
        this.assistManager = assistManager;
    }

    public void inject(List<SearchCard> cards, SearchQuery query) {
        // Leading injected cards
        if (query.getFrom() == 0) {
            cards.addAll(0, getLeadingInjectedCards(cards.isEmpty(), query));
        }
    }

    private List<SearchCard> getLeadingInjectedCards(boolean isEmpty, SearchQuery query) {
        List<SearchCard> injectedList = new ArrayList<>();

        injectNoLocationCard(isEmpty, query).ifPresent(injectedList::add);
        injectContainerCard(isEmpty, query).ifPresent(injectedList::add);
        injectNewestPlaceCard(isEmpty, query).ifPresent(injectedList::add);
        injectedList.addAll(injectNoResultCard(isEmpty, query));
        injectHeaderCard(isEmpty, query).ifPresent(injectedList::add);
        return injectedList;
    }

    private Optional<SearchCard> injectNoLocationCard(boolean isEmpty, SearchQuery query) {
        // if noLatLng = NoLocationCard
        if (query.getLatLng() != null) return Optional.empty();
        return Optional.of(CARD_NO_LOCATION);
    }

    private Optional<SearchCard> injectContainerCard(boolean isEmpty, SearchQuery query) {
        if (isEmpty) return Optional.empty();
        String latLng = getLatLngContext(query);
        if (latLng == null) return Optional.empty();

        List<Container> containers = assistManager.getNearbyContainer(latLng, 800, 15);
        if (containers.isEmpty()) return Optional.empty();

        return Optional.of(new SearchContainersCard(containers));
    }

    private Optional<SearchCard> injectNewestPlaceCard(boolean isEmpty, SearchQuery query) {
        if (isEmpty) return Optional.empty();
        String latLng = getLatLngContext(query);
        if (latLng == null) return Optional.empty();

        List<Place> newestPlaces = assistManager.getNewestPlaces(latLng, 1000, 20);
        if (newestPlaces.isEmpty()) return Optional.empty();

        return Optional.of(new SearchNewestPlacesCard(newestPlaces));
    }

    private List<SearchCard> injectNoResultCard(boolean isEmpty, SearchQuery query) {
        if (!isEmpty) return List.of();
        if (isLocationAnywhere(query)) return List.of(CARD_NO_RESULT);

        if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
        // Inject No Result Location Card with location name
        SearchNoResultLocationCard noResultCard = new SearchNoResultLocationCard();
        noResultCard.setLocationName(getLocationName(query));
        query.getFilter().setLocation(LOCATION_SINGAPORE);
        query.getFilter().setContainers(List.of());
        noResultCard.setSearchQuery(query);

        SearchQueryReplaceCard replaceCard = new SearchQueryReplaceCard();
        replaceCard.setSearchQuery(query);
        return List.of(noResultCard, replaceCard);
    }

    private Optional<SearchCard> injectHeaderCard(boolean isEmpty, SearchQuery query) {
        if (isEmpty) return Optional.empty();
        if (isComplexQuery(query)) return Optional.empty();

        // Contains search result & query is not complex
        SearchHeaderCard headerCard = new SearchHeaderCard();

        if (isLocationAnywhere(query)) {
            headerCard.setTitle("Popular In Singapore");
        } else if (isLocationNearby(query)) {
            headerCard.setTitle("Popular Near You");
        }
        return Optional.of(headerCard);
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

    /**
     * @param query query to find latlng
     * @return latLng of where the search is applied
     */
    private static String getLatLngContext(SearchQuery query) {
        // Condition checks
        if (StringUtils.isNotBlank(query.getQuery())) return null;
        if (query.getSort() != null) {
            String sortType = query.getSort().getType();
            if (StringUtils.isNotBlank(sortType) && !SearchQuery.Sort.TYPE_MUNCH_RANK.equals(sortType)) {
                return null;
            }
        }

        if (query.getFilter() != null) {
            SearchQuery.Filter filter = query.getFilter();
            if (filter.getContainers() != null) {
                if (!filter.getContainers().isEmpty()) return null;
            }

            if (filter.getLocation() != null) {
                if ("singapore".equals(filter.getLocation().getId())) return null;
                return filter.getLocation().getLatLng();
            }
        }

        return query.getLatLng();
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

    private static boolean isLocationNearby(SearchQuery query) {
        if (query.getLatLng() == null) return false;
        if (query.getFilter() == null) return true;
        // Location Exist == false
        if (query.getFilter().getLocation() != null) return false;
        if (query.getFilter().getContainers() == null) return true;
        // Container Exist == false
        if (query.getFilter().getContainers().isEmpty()) return true;
        return true;
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

    private static boolean isComplexQuery(SearchQuery query) {
        if (StringUtils.isNotBlank(query.getQuery())) return true;
        if (query.getSort() != null) {
            if (query.getSort().getType() != null)
                if (!query.getSort().getType().equals(SearchQuery.Sort.TYPE_MUNCH_RANK)) return true;
        }
        if (query.getFilter() != null) {
            if (query.getFilter().getContainers() != null) {
                if (!query.getFilter().getContainers().isEmpty()) return true;
            }
            if (query.getFilter().getHour() != null) {
                if (query.getFilter().getHour().getDay() != null) return true;
            }
            if (query.getFilter().getPrice() != null) {
                if (query.getFilter().getPrice().getMin() != null) return true;
                if (query.getFilter().getPrice().getMax() != null) return true;
            }
            if (query.getFilter().getTag() != null) {
                if (query.getFilter().getTag().getPositives() != null) {
                    if (!query.getFilter().getTag().getPositives().isEmpty()) return true;
                }
                if (query.getFilter().getTag().getNegatives() != null) {
                    if (!query.getFilter().getTag().getNegatives().isEmpty()) return true;
                }
            }
        }

        return false;
    }
}