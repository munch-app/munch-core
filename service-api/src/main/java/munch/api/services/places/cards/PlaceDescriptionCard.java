package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 9/11/2017
 * Time: 12:34 AM
 * Project: munch-core
 */
public final class PlaceDescriptionCard extends AbstractPlaceCard {
    private String description;

    @Override
    public String getCardId() {
        return "basic_Description_20171109";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
