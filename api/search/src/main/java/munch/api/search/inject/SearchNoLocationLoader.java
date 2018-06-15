package munch.api.search.inject;

import munch.api.search.cards.SearchNoLocationCard;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:29 AM
 * Project: munch-core
 */
public final class SearchNoLocationLoader implements SearchCardInjector.Loader {
    private static final SearchNoLocationCard CARD_NO_LOCATION = new SearchNoLocationCard();

    @Override
    public List<Position> load(Request request) {
        // if noLatLng = NoLocationCard
        if (request.getQuery().getLatLng() != null) return List.of();

        return of(-10_000, CARD_NO_LOCATION);
    }

}
