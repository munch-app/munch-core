package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 10:20 AM
 * Project: munch-core
 */
public enum AffiliateStatus {

    /**
     * Affiliate is pending, waiting for linking to place.
     */
    PENDING("PENDING"),

    /**
     * Affiliate is linked to place, meaning it's serving live now
     */
    LINKED("LINKED"),

    /**
     * Affiliate was once linked but dropped its link due to major changes in information provided.
     * - Basically, dropped for safety reason.
     */
    DROPPED("DROPPED"),

    /**
     * Affiliate is deleted from the source.
     */
    DELETED_SOURCE("DELETED_SOURCE"),

    /**
     * Affiliate is delete by munch.
     * - Usually used to indicate affiliate as invalid or error prone.
     */
    DELETED_MUNCH("DELETED_MUNCH"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    AffiliateStatus(String value) {
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
     * @return AffiliateStatus corresponding to the value
     */
    @JsonCreator
    public static AffiliateStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(AffiliateStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<AffiliateStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
