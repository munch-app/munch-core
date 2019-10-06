package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Date: 26/9/19
 * Time: 10:02 pm
 * @author Fuxing Loh
 */
public enum ProfileSocialStatus {

    CONNECTED("CONNECTED"),

    DISCONNECTED("DISCONNECTED"),

    UNKNOWN_ERROR("UNKNOWN_ERROR"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ProfileSocialStatus(String value) {
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
     * @return ProfileSocialStatusType corresponding to the value
     */
    @JsonCreator
    public static ProfileSocialStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ProfileSocialStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<ProfileSocialStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
