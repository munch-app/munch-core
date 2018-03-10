package munch.api.services.places.cards;

/**
 * Created by: Fuxing
 * Date: 11/3/2018
 * Time: 12:10 AM
 * Project: munch-core
 */
public final class PlaceClosedCard extends AbstractPlaceCard {
    private String reason;

    @Override
    public String getCardId() {
        return "basic_Closed_20180311";
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
