package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:08 PM
 * Project: munch-core
 */
public final class PlaceLocationCard extends AbstractPlaceCard {
    private String placeName;
    private String latLng;
    private String address;

    @Override
    public String getCardId() {
        return "basic_Location_20170924";
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
