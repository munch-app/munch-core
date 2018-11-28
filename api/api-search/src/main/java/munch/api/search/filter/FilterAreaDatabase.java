package munch.api.search.filter;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.data.client.AreaClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 28/11/18
 * Time: 4:40 AM
 * Project: munch-core
 */
@Singleton
public final class FilterAreaDatabase {
    private final Supplier<List<FilterArea>> supplier;

    @Inject
    public FilterAreaDatabase(AreaClient areaClient) {
        this.supplier = Suppliers.memoizeWithExpiration(() -> {
            List<FilterArea> areas = new ArrayList<>();
            areaClient.iterator().forEachRemaining(area -> {
                areas.add(new FilterArea(area.getAreaId(), area.getName()));
            });
            return areas;
        }, 12, TimeUnit.HOURS);

        // Preload
        this.supplier.get();
    }

    public List<FilterArea> get() {
        return supplier.get();
    }
}
