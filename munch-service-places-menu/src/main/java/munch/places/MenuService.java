package munch.places;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.Menu;
import munch.places.data.MenuDatabase;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:50 PM
 * Project: munch-core
 */
@Singleton
public class MenuService implements JsonService {

    private final MenuDatabase database;

    @Inject
    public MenuService(MenuDatabase database) {
        this.database = database;
    }

    @Override
    public void route() {
        PATH("/places/:placeId/menu", () -> {
            GET("", this::query);
            GET("/:menuId", this::get);
        });
    }

    private List<Menu> query(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return database.query(placeId, from, size);
    }

    private Menu get(JsonCall call) {
        // String placeId = call.pathString("placeId");
        String menuId = call.pathString("menuId");
        return database.get(menuId);
    }
}
