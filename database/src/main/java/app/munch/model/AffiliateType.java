package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 28/8/19
 * Time: 5:07 PM
 * Project: munch-core
 */
public enum AffiliateType {
    CHOPE_BOOK("CHOPE_BOOK"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    AffiliateType(String value) {
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
     * @return PlaceAffiliateType corresponding to the value
     */
    @JsonCreator
    public static AffiliateType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(AffiliateType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<AffiliateType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
