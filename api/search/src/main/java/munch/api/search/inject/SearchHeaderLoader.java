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
        if (request.getCardsCount() == 0) return List.of();
        if (request.hasArea()) return List.of();
        if (request.isComplex()) return List.of();
        if (request.getFrom() != 0) return List.of();

        // Contains search result & query is not complex
        SearchHeaderCard headerCard = new SearchHeaderCard();

        if (request.isAnywhere()) {
            headerCard.setTitle("Discover Singapore");
        } else if (request.isNearby()) {
            headerCard.setTitle("Discover Near You");
        } else {
            String location = request.getLocationName("Location");
            headerCard.setTitle("Discover " + location);
        }
        return of(-1, headerCard);
    }
}
