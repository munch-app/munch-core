package munch.api.search.plugin;

import munch.api.search.cards.SearchHeaderCard;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:32 AM
 * Project: munch-core
 */
public final class SearchHeaderPlugin implements SearchCardPlugin {

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (request.getCardsCount() == 0) return null;
        if (request.isComplex()) return null;

        if (request.getRequest().isAnywhere()) {
            // Is Anywhere default to Discover Singapore
            return of(-1, new SearchHeaderCard("Discover Singapore"));
        } else if (request.getRequest().isNearby()) {
            // Is Nearby default to Discover Near You
            return of(-1, new SearchHeaderCard("Discover Near You"));
        }

        return null;
    }
}
