package munch.catalyst.builder.place;

import munch.catalyst.data.Article;
import munch.catalyst.data.ImageMeta;
import munch.catalyst.data.InstagramMedia;
import munch.catalyst.data.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Curate the best top 10 images for munch-core to display
 * All the methods are static as they have no need to state
 */
public final class ImageCurator {
    private static final Logger logger = LoggerFactory.getLogger(ImageCurator.class);
    private static final int MAX_SIZE = 10;

    /**
     * TODO: PD-4: Select image properly with curating
     *
     * @param medias   medias
     * @param articles article
     * @return list of image that is >= ImageBuilder.MAX_SIZE
     */
    public static List<Place.Image> selectFrom(List<InstagramMedia> medias, List<Article> articles) {
        List<Place.Image> images = new ArrayList<>();

        for (InstagramMedia media : medias) {
            Place.Image image = create("instagram", media.getImage());
            if (image != null) images.add(image);
            if (images.size() >= MAX_SIZE) return images;
        }


        for (Article article : articles) {
            Place.Image image = create("article", article.getThumbnail());
            if (image != null) images.add(image);
            if (images.size() >= MAX_SIZE) return images;
        }

        return images;
    }

    private static Place.Image create(String from, ImageMeta meta) {
        if (meta == null) return null;

        Place.Image image = new Place.Image();
        image.setFrom(from);
        image.setKey(meta.getKey());
        image.setImages(meta.getImages());
        return image;
    }
}
