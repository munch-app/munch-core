package app.munch.elastic;

import app.munch.model.ElasticDocumentType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Fuxing Loh
 * @since 2019-11-27 at 11:33
 */
public enum ElasticIndex {
    SEARCH("search", Set.of(
            // Goes to Place page
            ElasticDocumentType.PLACE,

            // Goes to place index with location filtered
            ElasticDocumentType.LOCATION,

            // Goes to Article page
            ElasticDocumentType.ARTICLE,

            // Goes to place index with tag filtered
            ElasticDocumentType.TAG
    )),

    PLACE("place", Set.of(
            ElasticDocumentType.PLACE
    )),
    ;

    private final String value;
    private final Set<ElasticDocumentType> types;

    ElasticIndex(String value, Set<ElasticDocumentType> types) {
        this.value = value;
        this.types = types;
    }

    public String getValue() {
        return value;
    }

    public Set<ElasticDocumentType> getTypes() {
        return types;
    }

    private static final Map<ElasticDocumentType, Set<ElasticIndex>> mappings = _mappings();

    public static Set<ElasticIndex> getIndexes(ElasticDocumentType type) {
        return mappings.getOrDefault(type, Set.of());
    }

    private static Map<ElasticDocumentType, Set<ElasticIndex>> _mappings() {
        Map<ElasticDocumentType, Set<ElasticIndex>> mappings = new HashMap<>();

        for (ElasticIndex index : ElasticIndex.values()) {
            index.getTypes().forEach(type -> {
                Set<ElasticIndex> indexes = mappings.computeIfAbsent(type, t -> {
                    return new HashSet<>();
                });
                indexes.add(index);
            });
        }

        return mappings;
    }
}
