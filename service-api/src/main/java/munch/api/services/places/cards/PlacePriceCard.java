package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 19/12/2017
 * Time: 2:01 PM
 * Project: munch-core
 */
public final class PlacePriceCard extends AbstractPlaceCard {
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getCardId() {
        // Price is per person
        return "basic_Price_20171219";
    }
}
