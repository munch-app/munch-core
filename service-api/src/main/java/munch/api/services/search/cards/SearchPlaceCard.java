package munch.api.services.search.cards;

import munch.data.Place;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:57 PM
 * Project: munch-core
 */
public final class SearchPlaceCard extends SearchCard {

    private Place place;

    @Override
    public String getCardId() {
        return "basic_Place_13092017";
    }

    /**
     * @return data of the card
     */
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
