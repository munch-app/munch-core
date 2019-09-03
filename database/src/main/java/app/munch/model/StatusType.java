package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 2019-08-06
 * Time: 18:54
 */
public enum StatusType {

    /**
     * Default status, Place is open.
     */
    OPEN("OPEN"),

    /**
     * This place will not show up in searches for the public.
     * Indicates this place has been inactive for awhile.
     */
    DORMANT("DORMANT"),

    /**
     * This place will not show up in public searches.
     * This place is create but hidden from public searches.
     * Reasons varies but mostly is because there is no authority to this data.
     * - No reliable profile that created it
     * - Hidden data can be upgraded to Open if validated.
     *
     * However this place should still show up in non-public search.
     */
    HIDDEN("HIDDEN"),

    PERMANENTLY_CLOSED("PERMANENTLY_CLOSED"),

    DELETED("DELETED"),

    /**
     * Moved, Renamed, Redirected
     */
    MOVED("MOVED"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    StatusType(String value) {
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
     * @return StatusType corresponding to the value
     */
    @JsonCreator
    public static StatusType fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(StatusType.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<StatusType> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
