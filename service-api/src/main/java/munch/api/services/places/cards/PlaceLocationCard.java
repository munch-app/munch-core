package munch.api.services.places.cards;

import munch.data.Place;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:08 PM
 * Project: munch-core
 */
public final class PlaceLocationCard extends PlaceCard {

    private String name;
    private Place.Location location;

    @Override
    public String getCardId() {
        return "basic_Location_15092017";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Place.Location getLocation() {
        return location;
    }

    public void setLocation(Place.Location location) {
        this.location = location;
    }
}
