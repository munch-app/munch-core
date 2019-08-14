package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * All top level node requires a 13 char length id.
 * First 12 char is randomly generated using {@code KeyUtils.nextL12()}
 * Last char is fixed based on the type of the data.
 * <p>
 * All characters should follow: CrockfordBase32.
 * '0123456789abcdefghjkmnpqrstvwxyz'
 * In the scenario where all 32 characters is used, increase char length to 14.
 * <p>
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 2:13 pm
 */
public enum NodePostfix {
    PLACE("0"),
    ARTICLE("1"),
    LOCATION("2"),
    BRAND("3"),
    TAG("4"),
    PUBLICATION("5"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    NodePostfix(String value) {
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
     * @return NodePostfix corresponding to the value
     */
    @JsonCreator
    public static NodePostfix fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(NodePostfix.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<NodePostfix> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
