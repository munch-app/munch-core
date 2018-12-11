package munch.api.search.plugin.home;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchHomeRecentPlaceCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.restful.core.NextNodeList;
import munch.user.client.UserRecentPlaceClient;
import munch.user.data.UserRecentPlace;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 4:42 PM
 * Project: munch-core
 */
@Singleton
public final class SearchHomeRecentPlacePlugin implements SearchCardPlugin {

    private final UserRecentPlaceClient recentPlaceClient;
    private final PlaceCachedClient placeCachedClient;

    @Inject
    public SearchHomeRecentPlacePlugin(UserRecentPlaceClient recentPlaceClient, PlaceCachedClient placeCachedClient) {
        this.recentPlaceClient = recentPlaceClient;
        this.placeCachedClient = placeCachedClient;
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isFeature(SearchQuery.Feature.Home)) return null;
        if (!request.isFirstPage()) return null;

        String userId = request.getRequest().getUserId();
        if (userId == null) return null;

        NextNodeList<UserRecentPlace> recentPlaces = recentPlaceClient.list(userId, null, 20);
        if (recentPlaces.isEmpty()) return null;

        Map<String, Place> map = placeCachedClient.get(recentPlaces.stream().map(UserRecentPlace::getPlaceId));
        List<Place> places = recentPlaces.stream().map(rp -> map.get(rp.getPlaceId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return of(-100, new SearchHomeRecentPlaceCard(places));
    }
}
