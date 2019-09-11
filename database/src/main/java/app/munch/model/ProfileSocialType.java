package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 7:27 pm
 */
public enum ProfileSocialType {
    INSTAGRAM("INSTAGRAM"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ProfileSocialType(String value) {
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
     * @return SocialType corresponding to the value
     */
    @JsonCreator
    public static ProfileSocialType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ProfileSocialType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<ProfileSocialType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
