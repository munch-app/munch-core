package munch.api.search.cards;

import munch.data.location.Area;
import munch.data.place.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 5:15 PM
 * Project: munch-core
 */
public final class SearchLocationAreaCard implements SearchCard {

    private final Area area;
    private final List<Place> places;

    public SearchLocationAreaCard(Area area, List<Place> places) {
        this.area = area;
        this.places = places;
    }

    @Override
    public String getCardId() {
        return "LocationArea_2018-12-10";
    }

    @Override
    public String getUniqueId() {
        return area.getAreaId();
    }
}
