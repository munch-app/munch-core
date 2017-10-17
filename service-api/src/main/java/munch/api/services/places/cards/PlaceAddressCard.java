package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 24/9/2017
 * Time: 4:31 PM
 * Project: munch-core
 */
public final class PlaceAddressCard extends AbstractPlaceCard {
    private String address;

    private String street;
    private String unitNumber;
    private String city;
    private String country;
    private String postal;

    private String latLng;
    private String nearestTrain;

    @Override
    public String getCardId() {
        return "basic_Address_20170924";
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getNearestTrain() {
        return nearestTrain;
    }

    public void setNearestTrain(String nearestTrain) {
        this.nearestTrain = nearestTrain;
    }
}
