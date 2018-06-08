package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 17/11/17
 * Time: 3:53 PM
 * Project: munch-core
 */
public final class PlacePhoneCard extends AbstractPlaceCard {
    private String phone;

    @Override
    public String getCardId() {
        return "basic_Phone_20171117";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
