package munch.api.search.cards;

import munch.data.location.Area;
import munch.data.place.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 25/9/18
 * Time: 2:16 PM
 * Project: munch-core
 */
public final class SearchBetweenAreaCard implements SearchCard {

    private int index;

    private Area area;
    private List<Place> places;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public String getCardId() {
        return "injected_BetweenArea_20180925";
    }

    @Override
    public String getUniqueId() {
        return getCardId() + "_" + index;
    }
}
