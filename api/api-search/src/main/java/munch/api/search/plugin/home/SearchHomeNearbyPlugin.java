package munch.api.search.plugin.home;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchHomeNearbyCard;
import munch.api.search.plugin.SearchCardPlugin;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/12/18
 * Time: 12:07 AM
 * Project: munch-core
 */
@Singleton
public final class SearchHomeNearbyPlugin implements SearchCardPlugin {
    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Home)) return null;

        return of(-10, new SearchHomeNearbyCard());
    }
}
