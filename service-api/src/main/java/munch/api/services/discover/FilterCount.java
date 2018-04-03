package munch.api.services.discover;

import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 3/4/2018
 * Time: 4:15 AM
 * Project: munch-core
 */
public final class FilterCount {
    private long count;
    private Map<String, Integer> tags;

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
}
