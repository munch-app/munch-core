package munch.api.search.cards;

import munch.data.location.Area;

/**
 * Created by: Fuxing
 * Date: 25/9/18
 * Time: 2:16 PM
 * Project: munch-core
 */
public final class SearchBetweenClusterHeaderCard implements SearchCard {
    // TODO, What kind of data do i need for this card?

    private Area area;
    private int index;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String getCardId() {
        return "injected_BetweenClusterHeader_20180925";
    }

    @Override
    public String getUniqueId() {
        return getCardId() + "_" + index;
    }
}
