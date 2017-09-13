package munch.data.places;

import munch.data.Place;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:08 PM
 * Project: munch-core
 */
public final class PlaceLocationCard extends PlaceCard {

    private Place.Location location;

    @Override
    public String getId() {
        return "basic_Location_12092017";
    }

    public Place.Location getLocation() {
        return location;
    }

    public void setLocation(Place.Location location) {
        this.location = location;
    }
}
