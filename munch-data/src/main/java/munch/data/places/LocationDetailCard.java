package munch.data.places;

import munch.data.Place;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:08 PM
 * Project: munch-core
 */
public final class LocationDetailCard extends AbstractCard {

    private Place.Location location;

    @Override
    public String getId() {
        return "basic_LocationDetail_07092017";
    }

    public Place.Location getLocation() {
        return location;
    }

    public void setLocation(Place.Location location) {
        this.location = location;
    }
}
