package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 12:11 am
 */
public enum IndexType {
    PLACE("PLACE"),

    ARTICLE("ARTICLE"),

    TAG("TAG"),

    BRAND("BRAND"),

    LOCATION("LOCATION"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    IndexType(String value) {
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
     * @return IndexedType corresponding to the value
     */
    @JsonCreator
    public static IndexType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(IndexType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<IndexType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
