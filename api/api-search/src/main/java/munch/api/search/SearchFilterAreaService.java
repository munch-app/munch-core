package munch.api.search;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.api.ApiService;
import munch.data.client.AreaClient;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 15/6/18
 * Time: 10:55 PM
 * Project: munch-core
 */
@Singleton
public final class SearchFilterAreaService extends ApiService {

    private final Supplier<List<Area>> supplier;

    @Inject
    public SearchFilterAreaService(AreaClient areaClient) {
        this.supplier = Suppliers.memoizeWithExpiration(() -> {
            List<Area> areas = new ArrayList<>();
            areaClient.iterator().forEachRemaining(area -> {
                areas.add(new Area(area.getAreaId(), area.getName()));
            });
            return areas;
        }, 12, TimeUnit.HOURS);

        // Preload
        this.supplier.get();
    }

    @Override
    public void route() {
        PATH("/search/filter/areas", () -> {
            GET("", this::get);
        });
    }

    private List<Area> get(JsonCall call) {
        return supplier.get();
    }

    private static class Area {
        public String areaId;
        public String name;

        public Area(String areaId, String name) {
            this.areaId = areaId;
            this.name = name;
        }
    }
}
