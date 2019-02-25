package munch.api.search.plugin;

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
public final class SearchAreaClusterHeaderPlugin implements SearchCardPlugin {

    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return List.of();
        if (!request.getRequest().isWhere()) return List.of();

        List<Area> areas = request.getRequest().getAreas();
        if (areas.size() != 1) return List.of();

        Area area = areas.get(0);
        if (area.getType() != Area.Type.Cluster) return List.of();

        SearchAreaClusterHeaderCard card = new SearchAreaClusterHeaderCard();
        card.setArea(area);
        return of(-1, card);
    }
}
