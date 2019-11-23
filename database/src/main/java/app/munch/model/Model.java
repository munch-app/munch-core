package app.munch.model;

import dev.fuxing.utils.KeyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 09:56
 */
public interface Model {

    /**
     * @param name to convert into slug
     * @param max  number of character for slug, rest will be trimmed
     * @return generated slug
     */
    static String generateSlug(String name, int max) {
        return KeyUtils.generateSlug(name, max);
    }

    /**
     * @param synonyms to clean
     * @param limit    to maximum number of synonyms for the model
     * @return cleaned, limited synonyms
     */
    static Set<String> cleanSynonyms(Set<String> synonyms, int limit) {
        if (synonyms == null) return Set.of();

        return synonyms.stream()
                .map(StringUtils::lowerCase)
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .sorted(Comparator.comparingInt(String::length))
                .limit(limit)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
