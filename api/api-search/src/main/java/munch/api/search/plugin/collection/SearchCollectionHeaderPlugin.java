package munch.api.search.plugin.collection;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchCollectionHeaderCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.user.client.UserPlaceCollectionClient;
import munch.user.data.UserPlaceCollection;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/12/18
 * Time: 6:52 PM
 * Project: munch-core
 */
public final class SearchCollectionHeaderPlugin implements SearchCardPlugin {

    private final UserPlaceCollectionClient client;

    @Inject
    public SearchCollectionHeaderPlugin(UserPlaceCollectionClient client) {
        this.client = client;
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Collection)) return null;

        SearchQuery.Collection collection = request.getQuery().getCollection();
        if (collection == null) return null;
        if (collection.getCollectionId() == null) return null;

        UserPlaceCollection placeCollection = client.get(collection.getCollectionId());
        return of(-1000000, new SearchCollectionHeaderCard(placeCollection));
    }

}
