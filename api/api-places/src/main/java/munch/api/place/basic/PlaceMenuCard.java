package munch.api.place.basic;

import munch.data.place.Place;

/**
 * Created by: Fuxing
 * Date: 16/8/18
 * Time: 1:23 AM
 * Project: munch-core
 */
public final class PlaceMenuCard extends AbstractPlaceCard {
    private Place.Menu menu;

    @Override
    public String getCardId() {
        return "basic_Menu_20180816";
    }

    public Place.Menu getMenu() {
        return menu;
    }

    public void setMenu(Place.Menu menu) {
        this.menu = menu;
    }
}
