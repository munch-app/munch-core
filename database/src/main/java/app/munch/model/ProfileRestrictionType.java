package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 31/8/19
 * Time: 4:52 pm
 */
public enum ProfileRestrictionType {

    /**
     * Account is prevented from creating any article resources.
     */
    ARTICLE_WRITE("ARTICLE_WRITE"),

    /**
     * Account is prevented from creating a new place.
     */
    PLACE_WRITE("PLACE_WRITE"),

    /**
     * Account is prevented from creating place revision,
     * - cannot make any changes to existing place data.
     */
    PLACE_REVISION_WRITE("PLACE_REVISION_WRITE"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ProfileRestrictionType(String value) {
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
     * @return PlaceRevisionRuleType corresponding to the value
     */
    @JsonCreator
    public static ProfileRestrictionType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ProfileRestrictionType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<ProfileRestrictionType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
