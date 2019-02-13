package munch.api.search.plugin;

import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchNoLocationCard;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:29 AM
 * Project: munch-core
 */
public final class SearchNoLocationPlugin implements SearchCardPlugin {
    private static final SearchNoLocationCard CARD_NO_LOCATION = new SearchNoLocationCard();

    @Override
    public List<Position> load(Request request) {
        if (!request.getRequest().isFeature(SearchQuery.Feature.Search)) return null;
        if (request.getRequest().isWhere()) return null;
        if (request.getRequest().isBetween()) return null;
        if (!request.isFirstPage()) return null;
        if (request.getRequest().hasUserLatLng()) return null;

        return of(-10_000, CARD_NO_LOCATION);
    }
}
