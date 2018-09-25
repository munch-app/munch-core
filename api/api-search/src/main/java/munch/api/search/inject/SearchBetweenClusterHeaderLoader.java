package munch.api.search.inject;

import munch.api.search.cards.SearchBetweenClusterHeaderCard;
import munch.data.location.Area;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 25/9/18
 * Time: 2:12 PM
 * Project: munch-core
 */
@Singleton
public final class SearchBetweenClusterHeaderLoader implements SearchCardInjector.Loader {

    @Override
    public List<Position> load(Request request) {
        if (!request.isBetween()) return List.of();
        if (request.getAreas().size() <= request.getPage()) return List.of();

        Area area = request.getAreas().get(request.getPage());

        SearchBetweenClusterHeaderCard card = new SearchBetweenClusterHeaderCard();
        card.setArea(area);
        return of(-1, card);
    }
}
