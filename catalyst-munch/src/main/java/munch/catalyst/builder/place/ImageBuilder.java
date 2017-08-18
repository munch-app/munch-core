package munch.catalyst.builder.place;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import corpus.data.CorpusData;
import munch.data.ImageMeta;
import munch.data.Place;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Curate the best top 10 images for munch-core to display
 * All the methods are static as they have no need to state
 */
public final class ImageBuilder implements TypeBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ImageBuilder.class);
    private static final int MAX_SIZE = 20;

    private static final TypeReference<Map<String, String>> MapStringStringType = new TypeReference<Map<String, String>>() {
    };

    private final List<Pair<Long, Place.Image>> images = new ArrayList<>();
    private final ObjectMapper objectMapper = JsonUtils.objectMapper;

    @Override
    public boolean match(CorpusData.Field field) {
        return field.getKey().equals("Global.Munch.ImageCurator.image");
    }

    @Override
    public void add(CorpusData data, CorpusData.Field field) {
        Map<String, String> metadata = field.getMetadata();


        ImageMeta imageMeta = new ImageMeta();
        imageMeta.setKey(metadata.get("imageKey"));
        try {
            imageMeta.setImages(objectMapper.readValue(metadata.get("images"), MapStringStringType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Place.Image image = new Place.Image();
        image.setSource(metadata.get("imageSource"));
        image.setImageMeta(imageMeta);
        images.add(Pair.of(Long.parseLong(field.getValue()), image));
    }

    /**
     * @return list of image, in order of popularity
     */
    public List<Place.Image> collect() {
        return images.stream()
                .sorted((o1, o2) -> o2.getLeft().compareTo(o1.getLeft()))
                .map(Pair::getRight)
                .limit(MAX_SIZE)
                .collect(Collectors.toList());
    }
}
