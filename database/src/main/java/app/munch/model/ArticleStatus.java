package app.munch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 2019-08-14
 * Time: 04:13
 */
public enum ArticleStatus {

    /**
     * Article is still getting edited
     */
    DRAFT("DRAFT"),

    /**
     * Article is deleted
     */
    DELETED("DELETED"),

    /**
     * @see Groups.ArticlePublished
     */
    PUBLISHED("PUBLISHED"),

    UNKNOWN_TO_SDK_VERSION(null);

    private final String value;

    ArticleStatus(String value) {
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
     * @return ArticleStatus corresponding to the value
     */
    @JsonCreator
    public static ArticleStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(ArticleStatus.values()).filter(e -> e.toString().equals(value)).findFirst()
                .orElse(UNKNOWN_TO_SDK_VERSION);
    }

    public static Set<ArticleStatus> knownValues() {
        return Stream.of(values()).filter(v -> v != UNKNOWN_TO_SDK_VERSION).collect(Collectors.toSet());
    }
}
