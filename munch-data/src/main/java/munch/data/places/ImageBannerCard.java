package munch.data.places;

import munch.data.Place;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:45 AM
 * Project: munch-core
 */
public final class ImageBannerCard extends PlaceCard {

    private List<Place.Image> images;

    @Override
    public String getId() {
        return "basic_ImageBanner_11092017";
    }

    public List<Place.Image> getImages() {
        return images;
    }

    public void setImages(List<Place.Image> images) {
        this.images = images;
    }
}
