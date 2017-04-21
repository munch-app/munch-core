package munch.places;

import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import munch.struct.Menu;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 4:07 PM
 * Project: munch-core
 */
@Singleton
public class PlaceService implements JsonService {

    @Override
    public void route() {
        PATH("/places", () -> {
            GET("/:placeId", this::get);
        });
    }

    private List<Menu> get(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");

        // TODO query from some where

        return null;
    }
}
