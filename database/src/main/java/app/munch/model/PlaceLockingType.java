package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 31/8/19
 * Time: 5:01 pm
 */
public enum PlaceLockingType {

    /**
     * Locked by admin, only admin can make changes
     */
    ADMIN("ADMIN"),

    /**
     * Locked by an account, only that account can make changes.
     */
    CLAIMED("CLAIMED"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    PlaceLockingType(String value) {
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
     * @return PlaceRevisionProtocolLocked corresponding to the value
     */
    @JsonCreator
    public static PlaceLockingType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(PlaceLockingType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<PlaceLockingType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
