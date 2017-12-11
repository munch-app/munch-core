package munch.api.services.search.cards;


import munch.data.structure.Place;
import munch.data.structure.SourcedImage;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:57 PM
 * Project: munch-core
 */
public final class SearchPlaceCard implements SearchCard {
    private String placeId;
    private String name;
    private Place.Location location;

    private List<String> tags;
    private List<SourcedImage> images;
    private List<Place.Hour> hours;
    private Place.Review review;

    @Override
    public String getCardId() {
        return "basic_Place_20171211";
    }

    @Override
    public String getUniqueId() {
        return placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<SourcedImage> getImages() {
        return images;
    }

    public void setImages(List<SourcedImage> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Place.Location getLocation() {
        return location;
    }

    public void setLocation(Place.Location location) {
        this.location = location;
    }

    public List<Place.Hour> getHours() {
        return hours;
    }

    public void setHours(List<Place.Hour> hours) {
        this.hours = hours;
    }

    public Place.Review getReview() {
        return review;
    }

    public void setReview(Place.Review review) {
        this.review = review;
    }
}
