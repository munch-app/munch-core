package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 30/8/19
 * Time: 12:29 PM
 * Project: munch-core
 */
public enum WorkerTaskStatus {
    STARTED("STARTED"),

    RUNNING("RUNNING"),

    COMPLETED("COMPLETED"),

    ERROR("ERROR"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    WorkerTaskStatus(String value) {
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
     * @return WorkerReportStatus corresponding to the value
     */
    @JsonCreator
    public static WorkerTaskStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(WorkerTaskStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<WorkerTaskStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
