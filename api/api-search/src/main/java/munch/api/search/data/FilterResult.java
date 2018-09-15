package munch.api.search.data;

import java.util.Collections;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 10:53 PM
 * Project: munch-core
 */
public final class FilterResult {
    private long count;
    private Map<String, Integer> tags;
    private PriceGraph priceGraph;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Map<String, Integer> getTags() {
        return tags;
    }

    public void setTags(Map<String, Integer> tags) {
        this.tags = tags;
    }

    public PriceGraph getPriceGraph() {
        return priceGraph;
    }

    public void setPriceGraph(PriceGraph priceGraph) {
        this.priceGraph = priceGraph;
    }

    public static FilterResult from(Map<String, Integer> tags, Map<Double, Integer> frequency) {
        FilterResult result = new FilterResult();
        if (!tags.isEmpty()) {
            result.setTags(tags);
            result.setCount(Collections.max(tags.values()));
        }

        result.setPriceGraph(PriceGraph.fromFrequency(frequency));
        return result;
    }
}
