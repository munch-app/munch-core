package munch.api.search.filter;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.data.client.TagClient;
import munch.data.tag.Tag;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 28/11/18
 * Time: 7:48 AM
 * Project: munch-core
 */
@Singleton
public final class FilterTagDatabase {

    private final Supplier<Map<String, Tag>> supplier;

    @Inject
    public FilterTagDatabase(TagClient tagClient) {
        this.supplier = Suppliers.memoizeWithExpiration(() -> {
            Map<String, Tag> map = new HashMap<>();
            tagClient.iterator().forEachRemaining(tag -> {
                if (!tag.getSearch().isListed()) return;

                map.put(tag.getTagId(), tag);
            });
            return map;
        }, 13, TimeUnit.HOURS);

        // Preload
        this.supplier.get();
    }

    private List<FilterTag> getFilterTags(Map<String, Integer> countMap) {
        return supplier.get().values().stream()
                .map(tag -> {
                    FilterTag filterTag = new FilterTag();
                    filterTag.setTagId(tag.getTagId());
                    filterTag.setName(tag.getName());
                    filterTag.setType(tag.getType());
                    filterTag.setCount(countMap.getOrDefault(tag.getTagId(), 0));
                    return filterTag;
                })
                .collect(Collectors.toList());
    }

    @Nullable
    public FilterTag getTag(String tagId, int count) {
        Tag tag = supplier.get().get(tagId);
        if (tag == null) return null;


        FilterTag filterTag = new FilterTag();
        filterTag.setCount(count);
        filterTag.setTagId(tagId);
        filterTag.setName(tag.getName());
        filterTag.setType(tag.getType());
        return filterTag;
    }

    public FilterResult.TagGraph getGraph(Map<String, Integer> tags) {
        FilterResult.TagGraph tagGraph = new FilterResult.TagGraph();
        tagGraph.setTags(getFilterTags(tags));

        tagGraph.getTags().sort((o1, o2) -> Long.compare(o2.getCount(), o1.getCount()));
        return tagGraph;
    }
}
