package app.munch.vision;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fuxing Loh
 * @since 2019-10-18 at 12:44 am
 */
public enum VisionResultType {
    food("food"),

    menu("menu"),

    place("place"),

    block("block"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    VisionResultType(String value) {
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
     * @return VisionResultType corresponding to the value
     */
    @JsonCreator
    public static VisionResultType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(VisionResultType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<VisionResultType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
