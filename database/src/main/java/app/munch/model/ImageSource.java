package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 18:37
 */
public enum ImageSource {

    /**
     * Image uploaded when user is editing an article.
     */
    ARTICLE("ARTICLE"),

    /**
     * Image uploaded to places directly or when user is editing a place node in their written article.
     */
    PLACE("PLACE"),

    /**
     * Image uploaded directly to the library.
     */
    LIBRARY("LIBRARY"),

    /**
     * Image uploaded when user is changing profile pic.
     */
    PROFILE("PROFILE"),

    /**
     * Image syndicated from instagram.
     */
    INSTAGRAM("INSTAGRAM"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ImageSource(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    public static Set<ImageSource> fromQueryString(String value) {
        if (StringUtils.isBlank(value)) {
            return Set.of();
        }
        return Arrays.stream(value.split(", *"))
                .map(ImageSource::fromValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Use this in place of valueOf to convert the raw string returned by the service into the enum value.
     *
     * @param value real value
     * @return ImageSource corresponding to the value
     */
    @JsonCreator
    public static ImageSource fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ImageSource.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<ImageSource> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
