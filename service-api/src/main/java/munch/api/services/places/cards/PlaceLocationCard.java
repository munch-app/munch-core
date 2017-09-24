package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:08 PM
 * Project: munch-core
 */
public final class PlaceLocationCard extends PlaceCard {

    private String placeName;
    private String latLng;

    @Override
    public String getCardId() {
        return "basic_Location_24092017";
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
}
