package munch.api.search;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import munch.api.ApiService;
import munch.data.client.AreaClient;
import munch.data.location.Area;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
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

    private final Supplier<List<Area>> locationSupplier;
    private Long millis;

    @Inject
    public SearchFilterAreaService(AreaClient areaClient) {
        this.locationSupplier = Suppliers.memoizeWithExpiration(() -> {
            List<Area> areas = Lists.newArrayList(areaClient.iterator());
            millis = areas.stream()
                    .max(Comparator.comparingLong(Area::getUpdatedMillis))
                    .map(Area::getUpdatedMillis)
                    .orElse(null);
            return areas;
        }, 12, TimeUnit.HOURS);

        // Preload
        this.locationSupplier.get();
    }

    @Override
    public void route() {
        PATH("/search/filter/areas", () -> {
            GET("", this::get);
            HEAD("", this::head);
        });
    }

    private List<Area> get(JsonCall call) {
        List<Area> areas = locationSupplier.get();
        call.response().header("Last-Modified-Millis", String.valueOf(millis));
        return areas;
    }

    private JsonResult head(JsonCall call) {
        locationSupplier.get();
        call.response().header("Last-Modified-Millis", String.valueOf(millis));
        return JsonResult.ok();
    }
}
