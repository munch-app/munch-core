package munch.api.search.inject;

import munch.api.search.cards.SearchHeaderCard;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:32 AM
 * Project: munch-core
 */
public final class SearchHeaderLoader implements SearchCardInjector.Loader {

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return List.of();
        if (request.getCardsCount() == 0) return List.of();
        if (request.isComplex()) return List.of();

        if (request.isAnywhere()) {
            return header("Discover Singapore");
        } else if (request.isNearby()) {
            return header("Discover Near You");
        } else if (request.isWhere()) {
            String location = request.getLocationName("Location");
            return header("Discover " + location);
        } else {
            return List.of();
        }
    }

    private List<Position> header(String text) {
        SearchHeaderCard headerCard = new SearchHeaderCard();
        headerCard.setTitle(text);
        return of(-1, headerCard);
    }
}
