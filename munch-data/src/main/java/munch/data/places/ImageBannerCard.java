package munch.data.places;

import munch.data.ImageMeta;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:45 AM
 * Project: munch-core
 */
public final class ImageBannerCard extends AbstractCard {

    private ImageMeta image;

    @Override
    public String getId() {
        return "basic_ImageBanner_06092017";
    }

    public ImageMeta getImage() {
        return image;
    }

    public void setImage(ImageMeta image) {
        this.image = image;
    }
}
