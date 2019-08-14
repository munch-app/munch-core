package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 18:37
 */
public enum ImageSource {
    ARTICLE("ARTICLE"),

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
