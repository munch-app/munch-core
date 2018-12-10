package munch.api.search.cards;

import munch.data.place.Place;
import munch.user.data.UserPlaceCollection;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 8:00 PM
 * Project: munch-core
 */
public final class SearchHomePopularPlaceCard implements SearchCard {

    private final UserPlaceCollection collection;
    private final List<Place> places;

    public SearchHomePopularPlaceCard(UserPlaceCollection collection, List<Place> places) {
        this.collection = collection;
        this.places = places;
    }

    public UserPlaceCollection getCollection() {
        return collection;
    }

    public List<Place> getPlaces() {
        return places;
    }

    @Override
    public String getCardId() {
        return "HomePopularPlace_2018-12-10";
    }
}
