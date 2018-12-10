package munch.api.search.filter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.data.client.AreaClient;
import munch.data.client.ElasticClient;
import munch.data.elastic.ElasticUtils;
import munch.data.location.Area;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 28/11/18
 * Time: 4:40 AM
 * Project: munch-core
 */
@Singleton
public final class FilterAreaDatabase {
    private final Supplier<List<FilterArea>> supplier;
    private final ElasticClient elasticClient;

    @Inject
    public FilterAreaDatabase(AreaClient areaClient, ElasticClient elasticClient) {
        this.supplier = Suppliers.memoizeWithExpiration(() -> {
            List<FilterArea> areas = new ArrayList<>();
            areaClient.iterator().forEachRemaining(area -> {
                areas.add(new FilterArea(area.getAreaId(), area.getName(), area.getType()));
            });
            return areas;
        }, 12, TimeUnit.HOURS);
        this.elasticClient = elasticClient;

        // Preload
        this.supplier.get();
    }

    public List<FilterArea> get() {
        return supplier.get();
    }

    public List<FilterArea> search(String text, int size) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", size);

        ObjectNode bool = JsonUtils.createObjectNode();
        bool.set("must", ElasticUtils.match("name", text));
        bool.set("filter", ElasticUtils.filterTerm("dataType", "Area"));
        root.set("query", JsonUtils.createObjectNode().set("bool", bool));

        List<Area> areas = elasticClient.searchHitsHits(root);
        return areas.stream()
                .map(a -> new FilterArea(a.getAreaId(), a.getName(), a.getType()))
                .collect(Collectors.toList());
    }
}
