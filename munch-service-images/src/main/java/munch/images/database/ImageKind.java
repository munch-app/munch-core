package munch.images.database;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 11:07 PM
 * Project: munch-core
 */
public enum ImageKind {
    Original("original", 0, 0),
    Thumbnail("thumbnail", 0, 0);

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
     * Parse value to enum
     *
     * @param value value/name
     * @return parsed TypeDescription
     * @throws IllegalArgumentException if not found
     */
    @JsonCreator
    public static ImageKind forValue(String value) {
        if (Original.getName().equals(value))
            return Original;
        if (Thumbnail.getName().equals(value))
            return Thumbnail;
        throw new IllegalArgumentException("Type description not found.");
    }

    /**
     * Parse enum to value
     *
     * @param type enum
     * @return value
     */
    @JsonValue
    public static String toValue(ImageKind type) {
        return type.getName();
    }
}
