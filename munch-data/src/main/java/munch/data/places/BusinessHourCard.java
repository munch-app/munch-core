package munch.data.places;

import munch.data.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:05 PM
 * Project: munch-core
 */
public final class BusinessHourCard extends PlaceCard {

    private List<Place.Hour> hours;

    @Override
    public String getId() {
        return "basic_BusinessHour_07092017";
    }

    public List<Place.Hour> getHours() {
        return hours;
    }

    public void setHours(List<Place.Hour> hours) {
        this.hours = hours;
    }
}
