package munch.catalyst.builder.place;

import corpus.data.CorpusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created By: Fuxing Loh
 * Date: 5/7/2017
 * Time: 4:23 PM
 * Project: munch-core
 */
public class ValueBuilder implements TypeBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ValueBuilder.class);

    private Map<String, Map<String, Integer>> keyValueCount = new HashMap<>();

    @Override
    public void add(CorpusData data, CorpusData.Field field) {
        String key = field.getKey();
        String value = field.getValue();

        Map<String, Integer> values = keyValueCount.computeIfAbsent(key, v -> new HashMap<>());
        values.compute(value, (v, c) -> c == null ? 1 : c + 1);
    }

    /**
     * Collect all values for a single key
     *
     * @param key key
     * @return set of values, can be empty if none
     */
    public Set<String> collect(String key) {
        return keyValueCount.getOrDefault(key, Collections.emptyMap()).keySet();
    }

    /**
     * Collect a single value with most appearance
     *
     * @param key key to find
     * @return null if no values found for key
     */
    @Nullable
    public String collectMax(String key) {
        Map<String, Integer> values = keyValueCount.getOrDefault(key, Collections.emptyMap());
        return values.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Accepts all fields
     *
     * @param field field to match
     * @return always true
     */
    @Override
    public boolean match(CorpusData.Field field) {
        return true;
    }
}
