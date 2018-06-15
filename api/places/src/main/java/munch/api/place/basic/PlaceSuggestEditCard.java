package munch.api.place.basic;

/**
 * Created by: Fuxing
 * Date: 28/4/2018
 * Time: 6:19 PM
 * Project: munch-core
 */
public final class PlaceSuggestEditCard extends AbstractPlaceCard {
    private String placeId;
    private String name;
    private String address;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getCardId() {
        return "ugc_SuggestEdit_20180428";
    }
}
