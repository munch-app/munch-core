package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Date: 10/9/19
 * Time: 8:21 pm
 *
 * @author Fuxing Loh
 */
public enum MentionStatus {
    /**
     * Mention is public and linked to place.
     */
    PUBLIC("PUBLIC"),

    /**
     * Mention is possible and suggested.
     */
    SUGGESTED("SUGGESTED"),

    /**
     * Mention that was once linked but is now deleted.
     */
    DELETED("DELETED"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    MentionStatus(String value) {
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
     * @return MentionStatus corresponding to the value
     */
    @JsonCreator
    public static MentionStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(MentionStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<MentionStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
