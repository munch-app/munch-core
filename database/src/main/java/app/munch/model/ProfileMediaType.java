package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 26/9/19
 * Time: 7:57 pm
 */
public enum ProfileMediaType {
    INSTAGRAM_PHOTO("INSTAGRAM_PHOTO"),

    INSTAGRAM_VIDEO("INSTAGRAM_VIDEO"),

    INSTAGRAM_STORY("INSTAGRAM_STORY"),

    INSTAGRAM_ALBUM("INSTAGRAM_ALBUM"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ProfileMediaType(String value) {
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
     * @return SocialObjectType corresponding to the value
     */
    @JsonCreator
    public static ProfileMediaType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ProfileMediaType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<ProfileMediaType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
