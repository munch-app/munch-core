package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Date: 4/10/19
 * Time: 8:45 pm
 *
 * @author Fuxing Loh
 */
public enum ProfileMediaStatus {
    PENDING("PENDING"),

    PUBLIC("PUBLIC"),

    HIDDEN("HIDDEN"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ProfileMediaStatus(String value) {
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
     * @return ProfileMediaStatus corresponding to the value
     */
    @JsonCreator
    public static ProfileMediaStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ProfileMediaStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<ProfileMediaStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
