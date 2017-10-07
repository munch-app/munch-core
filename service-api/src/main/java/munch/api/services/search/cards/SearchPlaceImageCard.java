package munch.api.services.search.cards;

import munch.data.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:57 PM
 * Project: munch-core
 */
public final class SearchPlaceImageCard extends SearchCard {

    private String placeId;
    private String name;
    private String establishment;
    private Place.Location location;

    private List<String> tags;
    private List<Place.Image> images;
    private List<Place.Hour> hours;

    @Override
    public String getCardId() {
        return "basic_PlaceImage_06102017";
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<Place.Image> getImages() {
        return images;
    }

    public void setImages(List<Place.Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
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
}
