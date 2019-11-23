package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fuxing Loh
 * @since 2019-11-22 at 14:26
 */
public enum LocationType {
    CITY("CITY"),

    NEIGHBORHOOD("NEIGHBORHOOD"),

    OTHERS("OTHERS"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    LocationType(String value) {
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
     * @return LocationType corresponding to the value
     */
    @JsonCreator
    public static LocationType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(LocationType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<LocationType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
