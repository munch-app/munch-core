package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.lang3.RandomStringUtils;

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
 * Regex: [0-9a-hjkmnp-tv-z]
 * In the scenario where all 32 characters is used, increase char length to 14.
 * <p>
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 2:13 pm
 */
public enum L16Id {
    MEDIA("s"),
    MENTION("m"),
    POST("p"),

    UNKNOWN_TO_SDK_VERSION(null);

    public final String postfix;
    public final String regex;

    L16Id(String postfix) {
        this.postfix = postfix;
        this.regex = "^[0-9a-hjkmnp-tv-z]{15}" + postfix + "$";
    }

    /**
     * @return generate random id for current L16Id enum type
     */
    public String randomId() {
        return RandomStringUtils.random(15, KeyUtils.CROCKFORD_CHARCTERS) + postfix;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(postfix);
    }

    /**
     * Use this in place of valueOf to convert the raw string returned by the service into the enum value.
     *
     * @param value real value
     * @return NodePostfix corresponding to the value
     */
    @JsonCreator
    public static L16Id fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(L16Id.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<L16Id> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
