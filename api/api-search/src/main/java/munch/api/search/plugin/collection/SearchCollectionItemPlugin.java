package munch.api.search.plugin.collection;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchPlaceCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.data.client.PlaceCachedClient;
import munch.data.place.Place;
import munch.restful.core.NextNodeList;
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
 * Date: 11/12/18
 * Time: 6:52 PM
 * Project: munch-core
 */
@Singleton
public final class SearchCollectionItemPlugin implements SearchCardPlugin {

    private final UserPlaceCollectionClient client;
    private final PlaceCachedClient cachedClient;

    @Inject
    public SearchCollectionItemPlugin(UserPlaceCollectionClient client, PlaceCachedClient cachedClient) {
        this.client = client;
        this.cachedClient = cachedClient;
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isFeature(SearchQuery.Feature.Collection)) return null;

        SearchQuery.Collection collection = request.getQuery().getCollection();
        if (collection == null) return null;
        if (collection.getCollectionId() == null) return null;

        int page = request.getRequest().getPage();
        List<Place> places = load(collection.getCollectionId(), 40, page);

        if (places == null) return null;

        List<SearchPlaceCard> cards = places.stream().map(SearchPlaceCard::new)
                .collect(Collectors.toList());

        return of(0, cards);
    }

    @Nullable
    List<Place> load(String collectionId, int size, int page) {
        NextNodeList<UserPlaceCollection.Item> items = client.listItems(collectionId, null, size);

        for (int i = 0; i < page; i++) {
            Long sort = items.getNextLong("sort", null);

            // Return null = no more results
            if (sort == null) return null;

            items = client.listItems(collectionId, sort, size);
        }

        if (items.isEmpty()) return null;
        Map<String, Place> map = cachedClient.get(items.stream().map(UserPlaceCollection.Item::getPlaceId));

        return items.stream()
                .map(item -> map.get(item.getPlaceId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
