package munch.api.search.plugin;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchHomeTabCard;

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
        if (request.getRequest().isFeature(SearchQuery.Feature.Home)) {
            return of(0, new SearchHomeTabCard());
        }
        return null;
    }
}
