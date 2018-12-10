package munch.api.search.cards;

import munch.data.place.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 4:42 PM
 * Project: munch-core
 */
public final class SearchHomeRecentPlaceCard implements SearchCard {

    private final List<Place> places;

    public SearchHomeRecentPlaceCard(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    @Override
    public String getCardId() {
        return "HomeRecentPlace_2018-12-10";
    }
}
