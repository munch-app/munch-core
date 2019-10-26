package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fuxing Loh
 * @since 2019-10-26 at 15:58
 */
public enum PlacePostStatus {
    DRAFT("DRAFT"),

    PUBLISHED("PUBLISHED"),

    DELETED("DELETED"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    PlacePostStatus(String value) {
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
     * @return ProfilePostStatus corresponding to the value
     */
    @JsonCreator
    public static PlacePostStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(PlacePostStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<PlacePostStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
