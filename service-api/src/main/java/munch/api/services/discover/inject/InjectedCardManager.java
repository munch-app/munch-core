package munch.api.services.discover.inject;

import munch.api.services.discover.cards.*;
import munch.data.clients.LocationUtils;
import munch.data.structure.Container;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
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
    private static final SearchNoResultCard CARD_NO_RESULT = new SearchNoResultCard();
    private static final SearchNoLocationCard CARD_NO_LOCATION = new SearchNoLocationCard();

    private final MunchAssistManager assistManager;


    private final DiscoveryInstagramCardLoader instagramCardLoader;

    @Inject
    public InjectedCardManager(MunchAssistManager assistManager, DiscoveryInstagramCardLoader instagramCardLoader) {
        this.assistManager = assistManager;
        this.instagramCardLoader = instagramCardLoader;
    }

    public void inject(List<SearchCard> cards, SearchQuery query, @Nullable String userId) {
        int cardCount = cards.size();

        // Leading injected cards
        if (query.getFrom() == 0) {
            if (cardCount > 10) {
                // TODO Before Release change to 6
                // Also: Before Release change api.beta.munchapp.co to correct domain
                cards.addAll(1, instagramCardLoader.load());
            }

            cards.addAll(0, getLeadingInjectedCards(cardCount, query, userId));
        }
    }

    private List<SearchCard> getLeadingInjectedCards(int cardCount, SearchQuery query, @Nullable String userId) {
        List<SearchCard> injectedList = new ArrayList<>();

        injectedList.addAll(injectNoLocationCard(cardCount, query));
        injectedList.addAll(injectContainerCard(cardCount, query));
        injectedList.addAll(injectNoResultCard(cardCount, query));
        injectedList.addAll(injectHeaderCard(cardCount, query));
        return injectedList;
    }

    private List<SearchCard> injectNoLocationCard(int cardCount, SearchQuery query) {
        // if noLatLng = NoLocationCard
        if (query.getLatLng() != null) return List.of();
        return List.of(CARD_NO_LOCATION);
    }

    private List<SearchCard> injectContainerCard(int cardCount, SearchQuery query) {
        if (cardCount == 0) return List.of();
        if (isComplexQuery(query)) return List.of();

        String latLng = getLatLngContext(query);
        if (latLng == null) return List.of();

        List<Container> containers = assistManager.getNearbyContainer(latLng, 6);
        if (containers.isEmpty()) return List.of();

        return List.of(new SearchContainersCard(containers));
    }

    private Optional<SearchCard> injectNewestPlaceCard(int cardCount, SearchQuery query) {
        if (cardCount == 0) return Optional.empty();
        String latLng = getLatLngContext(query);
        if (latLng == null) return Optional.empty();
        if (isComplexQuery(query)) return Optional.empty();

        List<Place> newestPlaces = assistManager.getNewestPlaces(latLng, 1000, 20);
        if (newestPlaces.isEmpty()) return Optional.empty();

        return Optional.of(new SearchNewPlaceCard(newestPlaces));
    }

    private Optional<SearchCard> injectRecentPlaceCard(int cardCount, SearchQuery query, @Nullable String userId) {
        if (userId == null) return Optional.empty();
        if (cardCount == 0) return Optional.empty();
        if (!LocationUtils.isNearby(query)) return Optional.empty();
        if (isComplexQuery(query)) return Optional.empty();

        List<Place> recentPlaces = assistManager.getRecentPlaces(userId, 10);
        if (recentPlaces.isEmpty()) return Optional.empty();

        return Optional.of(new SearchRecentPlaceCard(recentPlaces));
    }

    private List<SearchCard> injectNoResultCard(int cardCount, SearchQuery query) {
        if (cardCount != 0) return List.of();
        if (LocationUtils.isAnywhere(query)) return List.of(CARD_NO_RESULT);

        if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
        // Inject No Result Location Card with location name
        SearchNoResultLocationCard noResultCard = new SearchNoResultLocationCard();
        noResultCard.setLocationName(getLocationName(query, "Nearby"));
        query.getFilter().setLocation(LocationUtils.SINGAPORE);
        query.getFilter().setContainers(List.of());
        noResultCard.setSearchQuery(query);

        SearchQueryReplaceCard replaceCard = new SearchQueryReplaceCard();
        replaceCard.setSearchQuery(query);
        return List.of(noResultCard, replaceCard);
    }

    private List<SearchCard> injectHeaderCard(int cardCount, SearchQuery query) {
        if (cardCount == 0) return List.of();
        if (isComplexQuery(query)) return List.of();
        if (query.getFrom() != 0) return List.of();

        // Contains search result & query is not complex
        SearchHeaderCard headerCard = new SearchHeaderCard();

        if (LocationUtils.isAnywhere(query)) {
            headerCard.setTitle("Discover Singapore");
        } else if (LocationUtils.isNearby(query)) {
            headerCard.setTitle("Discover Near You");
        } else {
            String location = getLocationName(query, "Location");
            headerCard.setTitle("Discover " + location);
        }
        return List.of(headerCard);
    }

    private static String getLocationName(SearchQuery query, String defaultName) {
        if (query.getFilter().getContainers() != null) {
            if (!query.getFilter().getContainers().isEmpty()) {
                return query.getFilter().getContainers().get(0).getName();
            }
        }
        if (query.getFilter().getLocation() != null) {
            return query.getFilter().getLocation().getName();
        }
        return defaultName;
    }

    /**
     * @param query query to find latlng
     * @return latLng of where the search is applied
     */
    private static String getLatLngContext(SearchQuery query) {
        // Condition checks
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

    private static boolean isComplexQuery(SearchQuery query) {
        if (query.getSort() != null) {
            if (query.getSort().getType() != null)
                if (!query.getSort().getType().equals(SearchQuery.Sort.TYPE_MUNCH_RANK)) return true;
        }
        if (query.getFilter() != null) {
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