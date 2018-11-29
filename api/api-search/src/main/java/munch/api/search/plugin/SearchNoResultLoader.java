package munch.api.search.plugin;

import munch.api.search.cards.SearchNoResultCard;

import java.util.List;


/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 1:48 AM
 * Project: munch-core
 */
public final class SearchNoResultLoader implements SearchCardPlugin {
    private static final SearchNoResultCard CARD_NO_RESULT = new SearchNoResultCard();

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return List.of();
        if (request.isCardsMoreThan(0)) return List.of();

        // Is Between will generate it's own No Result Card
        if (request.getRequest().isBetween()) return null;
        return of(10_000, CARD_NO_RESULT);
    }
}
