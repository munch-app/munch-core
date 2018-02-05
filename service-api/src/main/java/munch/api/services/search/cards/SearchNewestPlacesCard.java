package munch.api.services.search.cards;

import munch.data.structure.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 5/2/2018
 * Time: 4:41 PM
 * Project: munch-core
 */
public final class SearchNewestPlacesCard implements SearchCard {

    private List<Place> places;

    public SearchNewestPlacesCard(List<Place> places) {
        this.places = places;
    }

    @Override
    public String getCardId() {
        return "injected_NewestPlaces_20180205";
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
