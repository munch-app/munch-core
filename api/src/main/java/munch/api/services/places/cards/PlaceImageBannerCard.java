package munch.api.services.places.cards;


import munch.data.structure.SourcedImage;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:45 AM
 * Project: munch-core
 */
public final class PlaceImageBannerCard extends AbstractPlaceCard {
    private List<SourcedImage> images;

    @Override
    public String getCardId() {
        return "basic_ImageBanner_20170915";
    }

    public List<SourcedImage> getImages() {
        return images;
    }

    public void setImages(List<SourcedImage> images) {
        this.images = images;
    }
}
