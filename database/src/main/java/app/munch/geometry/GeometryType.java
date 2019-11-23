package app.munch.geometry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Only 3 geometry type are supported.
 *
 * @author Fuxing Loh
 * @since 2019-11-23 at 05:11
 */
public enum GeometryType {
    POINT("point"),

    POLYGON("polygon"),

    MULTIPOLYGON("multipolygon"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    GeometryType(String value) {
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
     * @return GeometryType corresponding to the value
     */
    @JsonCreator
    public static GeometryType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(GeometryType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<GeometryType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
