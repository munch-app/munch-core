package munch.api.search.cards;


import munch.data.place.Place;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:57 PM
 * Project: munch-core
 */
public final class SearchPlaceCard implements SearchCard {
    private final Place place;

    public SearchPlaceCard(Place place) {
        this.place = place;
    }

    @Override
    public String getCardId() {
        return "Place_2018-12-29";
    }

    @Override
    public String getUniqueId() {
        return place.getPlaceId();
    }

    public Place getPlace() {
        return place;
    }
}
