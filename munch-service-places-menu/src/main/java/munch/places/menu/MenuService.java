package munch.places.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.utils.file.FileEndpoint;
import munch.places.menu.data.Menu;
import munch.places.menu.data.MenuDatabase;
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
    private final FileEndpoint endpoint;

    @Inject
    public MenuService(MenuDatabase database, FileEndpoint endpoint) {
        this.database = database;
        this.endpoint = endpoint;
    }

    @Override
    public void route() {
        PATH("/places/:placeId/menu", () -> {
            GET("/list", this::list);
            GET("/:menuId", this::get);
        });
    }

    /**
     * @param call json call
     * @return 200 = List of Menu, else error
     */
    private List<Menu> list(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        List<Menu> menus = database.list(placeId, from, size);
        menus.forEach(this::resolveUrl);
        return menus;
    }

    /**
     * @param call json call
     * @return 200 = Menu, 404 = NotFound, else error
     */
    private Menu get(JsonCall call) {
        String menuId = call.pathString("menuId");
        Menu menu = database.get(menuId);
        resolveUrl(menu);
        return menu;
    }

    public void resolveUrl(Menu menu) {
        if (menu.getImage() != null) {
            for (Menu.ImageType type : menu.getImage().getKinds()) {
                type.setUrl(endpoint.getUrl(type.getKey()));
            }
        } else if (menu.getPdf() != null) {
            menu.getPdf().setUrl(endpoint.getUrl(menu.getPdf().getKey()));
        }
    }
}
