package munch.api.search.plugin.home;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchHomeTabCard;
import munch.api.search.plugin.SearchCardPlugin;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 29/11/18
 * Time: 11:43 AM
 * Project: munch-core
 */
@Singleton
public final class SearchHomeTabPlugin implements SearchCardPlugin {

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Home)) return null;

        return of(-1000, new SearchHomeTabCard());
    }
}
