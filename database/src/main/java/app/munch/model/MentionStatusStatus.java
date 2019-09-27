package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 8:21 pm
 */
public enum MentionStatusStatus {
    PENDING("PENDING"),

    PROFILE_LINKED("PROFILE_LINKED"),

    PROFILE_DELETED("PROFILE_DELETED"),

    MACHINE_LINKED("MACHINE_LINKED"),

    MACHINE_DELETED("MACHINE_DELETED"),

    MODERATOR_LINKED("MODERATOR_LINKED"),

    MODERATOR_DELETED("MODERATOR_DELETED"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    MentionStatusStatus(String value) {
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
    public static MentionStatusStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(MentionStatusStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<MentionStatusStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
