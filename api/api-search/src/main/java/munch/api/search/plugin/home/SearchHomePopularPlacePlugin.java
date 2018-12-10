package munch.api.search.plugin.home;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchHomePopularPlaceCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.user.client.UserPlaceCollectionClient;
import munch.user.data.UserPlaceCollection;

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
 * Time: 7:59 PM
 * Project: munch-core
 */
@Singleton
public final class SearchHomePopularPlacePlugin implements SearchCardPlugin {

    private final UserPlaceCollectionClient collectionClient;
    private final PlaceCachedClient placeClient;

    @Inject
    public SearchHomePopularPlacePlugin(UserPlaceCollectionClient collectionClient, PlaceCachedClient placeClient) {
        this.collectionClient = collectionClient;
        this.placeClient = placeClient;
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isFeature(SearchQuery.Feature.Home)) return null;
        if (!request.isFirstPage()) return null;

        UserPlaceCollection collection = collectionClient.get("cee9a5b7-00f1-4c18-98f5-3adac82ef9a2");
        if (collection == null) return null;

        List<UserPlaceCollection.Item> items = collectionClient.listItems(collection.getCollectionId(), null, 20);
        Map<String, Place> placeMap = placeClient.get(items.stream().map(UserPlaceCollection.Item::getPlaceId));
        List<Place> places = items.stream().map(item -> placeMap.get(item.getPlaceId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        SearchHomePopularPlaceCard card = new SearchHomePopularPlaceCard(collection, places);
        return of(-10, card);
    }
}
