package munch.api.search.cards;


import munch.data.place.Place;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:57 PM
 * Project: munch-core
 */
public class SearchPlaceCard implements SearchCard {
    private Place place;

    @Override
    public String getCardId() {
        return "basic_Place_20171211";
    }

    @Override
    public String getUniqueId() {
        return place.getPlaceId();
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public final static class Small extends SearchPlaceCard {

        @Override
        public String getCardId() {
            return "basic_SmallPlace_20180129";
        }
    }
}
