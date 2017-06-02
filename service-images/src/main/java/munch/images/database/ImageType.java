package munch.images.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import munch.restful.server.exceptions.StructuredException;
import org.apache.commons.lang3.StringUtils;

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
public enum ImageType {
    @JsonProperty("original")
    Original("original", 0, 0),

    @JsonProperty("150x150")
    X150("150x150", 150, 150),

    @JsonProperty("320x320")
    X320("320x320", 320, 320),

    @JsonProperty("640x640")
    X640("640x640", 640, 640),

    @JsonProperty("1080x1080")
    X1080("1080x1080", 1080, 1080);

    public static final Set<ImageType> DEFAULT_KINDS = ImmutableSet.of(X150, X320, X640, X1080);

    private final String name;
    private final int width;
    private final int height;

    /**
     * @param name   json name
     * @param width  width of image kind
     * @param height height of image kind
     */
    ImageType(String name, int width, int height) {
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
     * @throws NotFoundException if given kind is not found,
     *                           restful server knows how to handle this
     */
    public static ImageType forValue(String value) {
        switch (value) {
            case "original":
                return Original;
            case "150x150":
                return X150;
            case "320x320":
                return X320;
            case "640x640":
                return X640;
            case "1080x1080":
                return X1080;
            default:
                // Must have backward compatibility
                throw new NotFoundException(value);
        }
    }

    /**
     * @param queryString query string of kinds
     * @return Set of ImageKind
     */
    public static Set<ImageType> resolveKinds(@Nullable String queryString) {
        if (StringUtils.isBlank(queryString)) return ImmutableSet.of();
        return Arrays.stream(queryString.split(" *, *"))
                .map(ImageType::forValue)
                .collect(Collectors.toSet());
    }

    /**
     * Image kind not found
     *
     * @see ImageType for all the options available
     */
    public static class NotFoundException extends StructuredException {
        NotFoundException(String kind) {
            super("ImageKindNotFoundException", "ImageKind: " + kind + " not found.", 400);
        }
    }
}
