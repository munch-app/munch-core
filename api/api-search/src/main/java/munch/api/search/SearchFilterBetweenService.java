package munch.api.search;

import munch.api.ApiService;
import munch.api.search.data.BetweenLocation;
import munch.data.location.Area;
import munch.restful.server.JsonCall;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 24/9/18
 * Time: 6:51 PM
 * Project: munch-core
 */
@Singleton
public final class SearchFilterBetweenService extends ApiService {

    @Override
    public void route() {
        PATH("/search/filter/between", () -> {
            POST("/search", this::search);
            POST("/generate", this::generate);
        });
    }

    public List<BetweenLocation> search(JsonCall call) {
        // TODO: Search a list of Location
        return List.of();
    }

    public List<Area> generate(JsonCall call) {
        List<BetweenLocation> locations = call.bodyAsList(BetweenLocation.class);
        // TODO: Generate a list of Area
        return List.of();
    }
}
