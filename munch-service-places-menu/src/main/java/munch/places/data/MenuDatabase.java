package munch.places.data;

import com.google.inject.Singleton;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 9:05 PM
 * Project: munch-core
 */
@Singleton
public class MenuDatabase {

    public List<Menu> query(String placeId, int from, int size) {
        return null;
    }

    public void put(Menu menu) {
    }

    public Menu get(String menuId) {
        return null;
    }

    public void delete(String menuId) {

    }
}
