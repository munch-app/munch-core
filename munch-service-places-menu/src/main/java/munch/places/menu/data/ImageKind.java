package munch.places.menu.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 11:07 PM
 * Project: munch-core
 */
public enum ImageKind {
    @JsonProperty("original")
    Original("original", 0, 0),

    @JsonProperty("200x200")
    X200("200x200", 200, 200);

    private final String name;
    private final int width;
    private final int height;

    ImageKind(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    /**
     * @return name of image type
     */
    public String getName() {
        return name;
    }

    /**
     * @return width of image type
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return height of image type
     */
    public int getHeight() {
        return height;
    }

    /**
     * By default form original image this method is not to be used
     * original should be without kind name e.g. = qN0RpD6Tli2g63jzePOxGMR6m936OOov.jpg
     *
     * @param imageKey  image key
     * @param extension including period
     * @return imageKey_original.jpg
     * e.g. qN0RpD6Tli2g63jzePOxGMR6m936OOov_150x150.jpg
     * e.g. qN0RpD6Tli2g63jzePOxGMR6m936OOov_1080x1080.jpg
     */
    public String makeKey(String imageKey, String extension) {
        return imageKey + "_" + name + extension;
    }

    /**
     * Parse value to enum
     * Json mapping for string value too
     *
     * @param value value/name
     * @return parsed TypeDescription
     */
    public static ImageKind forValue(String value) {
        switch (value) {
            case "original":
                return Original;
            case "200x200":
                return X200;
            default:
                throw new IllegalArgumentException(value);
        }
    }

    /**
     * @param queryString query string of kinds
     * @return Set of ImageKind
     */
    public static Set<ImageKind> resolveKinds(@Nullable String queryString) {
        if (queryString == null) return ImmutableSet.of();
        return Arrays.stream(queryString.split(" *, *"))
                .map(ImageKind::forValue)
                .collect(Collectors.toSet());
    }
}
