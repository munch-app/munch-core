package munch.api.place.basic;


import munch.data.Hour;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 7/9/17
 * Time: 2:05 PM
 * Project: munch-core
 */
public final class PlaceBusinessHourCard extends AbstractPlaceCard {
    private List<Hour> hours;

    @Override
    public String getCardId() {
        return "basic_BusinessHour_20170907";
    }

    public List<Hour> getHours() {
        return hours;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }
}
