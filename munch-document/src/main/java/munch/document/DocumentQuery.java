package munch.document;

import munch.struct.Place;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:25 PM
 * Project: munch-core
 */
public interface DocumentQuery {

    /**
     * @param key key of place
     * @return Place with they key, nullable
     */
    Place get(String key);

    /**
     * @param keys list of key to query of a place
     * @return List of Place, non null values
     */
    List<Place> get(List<String> keys);
}
