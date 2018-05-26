package munch.api.services.search.inject;

import munch.api.services.search.cards.SearchNoResultCard;
import munch.api.services.search.cards.SearchNoResultLocationCard;
import munch.api.services.search.cards.SearchQueryReplaceCard;
import munch.data.clients.LocationUtils;
import munch.data.structure.SearchQuery;

import java.util.List;


/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 1:48 AM
 * Project: munch-core
 */
public final class SearchNoResultLoader implements SearchCardInjector.Loader {
    private static final SearchNoResultCard CARD_NO_RESULT = new SearchNoResultCard();

    @Override
    public List<Position> load(Request request) {
        if (request.getFrom() != 0) return List.of();
        if (request.isCardsMore(0)) return List.of();
        if (request.isAnywhere()) return of(10_000, CARD_NO_RESULT);

        SearchQuery query = request.cloneQuery();
        if (query.getFilter() == null) {
            query.setFilter(new SearchQuery.Filter());
        }

        // TODO SearchQuery Don't replace
        // Inject No Result Location Card with location name
        SearchNoResultLocationCard card = new SearchNoResultLocationCard();
        card.setLocationName(request.getLocationName("Nearby"));
        query.getFilter().setLocation(LocationUtils.SINGAPORE);
        query.getFilter().setContainers(List.of());
        card.setSearchQuery(query);

        return of(10_000, card, new SearchQueryReplaceCard(query));
    }
}
