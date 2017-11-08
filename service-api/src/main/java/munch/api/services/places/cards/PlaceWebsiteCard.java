package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 9/11/2017
 * Time: 12:36 AM
 * Project: munch-core
 */
public final class PlaceWebsiteCard extends AbstractPlaceCard {
    private String website;

    @Override
    public String getCardId() {
        return "basic_Website_20171109";
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
