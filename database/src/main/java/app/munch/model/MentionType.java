package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Date: 10/9/19
 * Time: 8:16 pm
 *
 * @author Fuxing Loh
 */
public enum MentionType {
    /**
     * @see ProfileMedia
     */
    MEDIA("MEDIA"),

    /**
     * @see Article
     */
    ARTICLE("ARTICLE"),

    /**
     * Not Yet Implemented
     */
    IMAGE("IMAGE"),

    /**
     * Not Yet Implemented
     */
    REVIEW("REVIEW"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    MentionType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    public static Set<MentionType> fromQueryString(String value) {
        if (StringUtils.isBlank(value)) {
            return Set.of();
        }
        return Arrays.stream(value.split(", *"))
                .map(MentionType::fromValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Use this in place of valueOf to convert the raw string returned by the service into the enum value.
     *
     * @param value real value
     * @return MentionType corresponding to the value
     */
    @JsonCreator
    public static MentionType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(MentionType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<MentionType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
