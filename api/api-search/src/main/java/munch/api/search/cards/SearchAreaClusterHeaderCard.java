package munch.api.search.cards;

import munch.data.location.Area;

/**
 * Created by: Fuxing
 * Date: 11/5/18
 * Time: 8:57 PM
 * Project: munch-core
 */
public final class SearchAreaClusterHeaderCard implements SearchCard {

    private Area area;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String getCardId() {
        return "injected_AreaClusterHeader_20180621";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }
}