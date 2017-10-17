package munch.api.services.places.cards;


import munch.data.structure.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:05 PM
 * Project: munch-core
 */
public final class PlaceBusinessHourCard extends AbstractPlaceCard {
    private List<Place.Hour> hours;

    @Override
    public String getCardId() {
        return "basic_BusinessHour_20170907";
    }

    public List<Place.Hour> getHours() {
        return hours;
    }

    public void setHours(List<Place.Hour> hours) {
        this.hours = hours;
    }
}
