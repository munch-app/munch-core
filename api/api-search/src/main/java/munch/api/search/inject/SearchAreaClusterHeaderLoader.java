package munch.api.search.inject;

import munch.api.search.cards.SearchAreaClusterHeaderCard;
import munch.data.location.Area;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 2:32 AM
 * Project: munch-core
 */
@Singleton
public final class SearchAreaClusterHeaderLoader implements SearchCardInjector.Loader {

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return List.of();
        if (!request.hasArea()) return List.of();

        Area area = request.getQuery().getFilter().getArea();
        if (area.getType() != Area.Type.Cluster) return List.of();

        SearchAreaClusterHeaderCard card = new SearchAreaClusterHeaderCard();
        card.setArea(area);
        return of(-1, card);
    }
}
