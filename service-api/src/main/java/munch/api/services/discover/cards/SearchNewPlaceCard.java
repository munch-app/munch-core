package munch.api.services.discover.cards;

import munch.data.structure.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 5/2/2018
 * Time: 4:41 PM
 * Project: munch-core
 */
public final class SearchNewPlaceCard implements SearchCard {

    private List<Place> places;

    public SearchNewPlaceCard(List<Place> places) {
        this.places = places;
    }

    @Override
    public String getCardId() {
        return "injected_NewPlace_20180209";
    }

    @Override
    public String getUniqueId() {
        return getCardId();
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
