package munch.api.search.filter;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.data.client.TagClient;
import munch.data.tag.Tag;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
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

    private final Supplier<List<Tag>> supplier;

    @Inject
    public FilterTagDatabase(TagClient tagClient) {
        this.supplier = Suppliers.memoizeWithExpiration(() -> {
            List<Tag> areas = new ArrayList<>();
            tagClient.iterator().forEachRemaining(tag -> {
                if (!tag.getSearch().isListed()) return;
                areas.add(tag);
            });
            return areas;
        }, 13, TimeUnit.HOURS);

        // Preload
        this.supplier.get();
    }

    public List<Tag> get() {
        return supplier.get();
    }

    private List<FilterTag> getFilterTags(Map<String, Integer> countMap) {
        return supplier.get().stream()
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

    public FilterResult.TagGraph getGraph(Map<String, Integer> tags) {
        FilterResult.TagGraph tagGraph = new FilterResult.TagGraph();
        tagGraph.setTags(getFilterTags(tags));

        tagGraph.getTags().sort((o1, o2) -> Long.compare(o2.getCount(), o1.getCount()));
        return tagGraph;
    }
}
