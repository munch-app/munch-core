package munch.catalyst.builder.place;

import corpus.data.CorpusData;
import munch.data.ImageMeta;
import munch.data.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Curate the best top 10 images for munch-core to display
 * All the methods are static as they have no need to state
 */
public final class ImageBuilder implements TypeBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ImageBuilder.class);
    private static final int MAX_SIZE = 20;

    private final List<Place.Image> images = new ArrayList<>();

    private static Place.Image create(String from, ImageMeta meta) {
        // Should be a builder not a curator any more
        // TODO with new corpus inputs
        if (meta == null) return null;

        Place.Image image = new Place.Image();
        image.setFrom(from);
        image.setImageMeta(meta);
        return image;
    }

    @Override
    public boolean match(CorpusData.Field field) {
        return field.getKey().startsWith("Global.Munch.ImageCurator.Image.");

    }

    @Override
    public void add(CorpusData data, CorpusData.Field field) {
        ImageMeta meta = new ImageMeta();
        meta.setCreated(data.getUpdatedDate());
        meta.setImages(field.getMetadata());
        meta.setKey(field.getValue());

        Place.Image image = new Place.Image();
        // TODO image from?
        images.add()
        // TODO
    }

    /**
     * @return list of image, in order of popularity
     */
    public List<Place.Image> collect() {
        return null;
    }
}
