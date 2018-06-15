package munch.api.place.basic;

import munch.data.Location;

/**
 * Created by: Fuxing
 * Date: 24/9/2017
 * Time: 4:31 PM
 * Project: munch-core
 */
public final class PlaceAddressCard extends AbstractPlaceCard {
    private Location location;

    @Override
    public String getCardId() {
        return "basic_Address_20180613";
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
