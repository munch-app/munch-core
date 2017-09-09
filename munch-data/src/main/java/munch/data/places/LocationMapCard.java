package munch.data.places;

import munch.data.Place;

/**
 * Created by: Fuxing
 * Date: 10/9/2017
 * Time: 2:06 AM
 * Project: munch-core
 */
public final class LocationMapCard extends PlaceCard {

    private Place.Location location;

    @Override
    public String getId() {
        return "basic_LocationMap_10092017";
    }

    public Place.Location getLocation() {
        return location;
    }

    public void setLocation(Place.Location location) {
        this.location = location;
    }
}
