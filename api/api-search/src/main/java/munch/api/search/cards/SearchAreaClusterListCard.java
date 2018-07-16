package munch.api.search.cards;

import munch.data.location.Area;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/12/2017
 * Time: 4:13 AM
 * Project: munch-core
 */
public final class SearchAreaClusterListCard implements SearchCard {

    private List<Area> areas;

    public SearchAreaClusterListCard(List<Area> areas) {
        this.areas = areas;
    }

    @Override
    public String getCardId() {
        return "injected_AreaClusterList_20180621";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
