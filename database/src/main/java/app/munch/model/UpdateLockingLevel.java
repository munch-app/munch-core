package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fuxing Loh
 * @since 6/10/19 at 6:47 pm
 */
public enum UpdateLockingLevel {

    /**
     * Admin, highest level of locking by someone in the Munch team.
     */
    ADMIN("ADMIN"),

    /**
     * Owner, locked by someone in that claimed it.
     */
    CLAIMED("CLAIMED"),

    /**
     * System, locked by machine.
     */
    SYSTEM("SYSTEM"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    UpdateLockingLevel(String value) {
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
     * @return UpdateLockingType corresponding to the value
     */
    @JsonCreator
    public static UpdateLockingLevel fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(UpdateLockingLevel.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<UpdateLockingLevel> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
