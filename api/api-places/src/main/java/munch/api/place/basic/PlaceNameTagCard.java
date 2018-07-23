package munch.api.place.basic;

import munch.data.place.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:50 AM
 * Project: munch-core
 */
public final class PlaceNameTagCard extends AbstractPlaceCard {
    private String name;
    private List<Place.Tag> tags;

    @Override
    public String getCardId() {
        return "basic_NameTag_20170912";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Place.Tag> getTags() {
        return tags;
    }

    public void setTags(List<Place.Tag> tags) {
        this.tags = tags;
    }
}
