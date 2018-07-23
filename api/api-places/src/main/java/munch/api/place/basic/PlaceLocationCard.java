package munch.api.place.basic;

import munch.data.Location;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:08 PM
 * Project: munch-core
 */
public final class PlaceLocationCard extends AbstractPlaceCard {
    private String placeName;
    private Location location;

    @Override
    public String getCardId() {
        return "basic_Location_20180613";
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
