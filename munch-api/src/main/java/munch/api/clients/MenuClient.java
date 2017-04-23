package munch.api.clients;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.api.struct.Menu;
import munch.restful.client.RestfulClient;

import javax.inject.Named;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 22/4/2017
 * Time: 9:47 PM
 * Project: munch-core
 */
@Singleton
public class MenuClient extends RestfulClient {

    @Inject
    public MenuClient(@Named("services") Config config) {
        super(config.getString("places-menu.url"));
    }

    /**
     * @param placeId place id
     * @param from    list start from
     * @param size    list size
     * @return List of Menu
     */
    public List<Menu> list(String placeId, int from, int size) {
        return doGet("/places/{placeId}/menu")
                .path("placeId", placeId)
                .queryString("from", from)
                .queryString("size", size)
                .asDataList(Menu.class);
    }
}
