package munch.api.services.places.cards;

import munch.data.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:45 AM
 * Project: munch-core
 */
public final class PlaceImageBannerCard extends PlaceCard {
    private List<Place.Image> images;

    @Override
    public String getCardId() {
        return "basic_ImageBanner_15092017";
    }

    public List<Place.Image> getImages() {
        return images;
    }

    public void setImages(List<Place.Image> images) {
        this.images = images;
    }
}