package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 28/8/19
 * Time: 6:27 PM
 * Project: munch-core
 */
public enum ProfileLinkType {
    INSTAGRAM("INSTAGRAM"),

    FACEBOOK("FACEBOOK"),

    OTHERS("OTHERS"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ProfileLinkType(String value) {
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
     * @return ProfileLinkType corresponding to the value
     */
    @JsonCreator
    public static ProfileLinkType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ProfileLinkType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static ProfileLinkType fromUrl(String url) {
        if (url == null) return null;

        try {
            URI uri = URI.create(url);
            switch (uri.getHost()) {
                case "instagram.com":
                case "www.instagram.com":
                    return INSTAGRAM;

                case "facebook.com":
                case "www.facebook.com":
                    return FACEBOOK;
            }
        } catch (Exception ignored) {
        }

        return OTHERS;
    }

    public static Set<ProfileLinkType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
