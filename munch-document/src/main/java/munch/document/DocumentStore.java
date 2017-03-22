package munch.document;

import munch.struct.Place;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:36 PM
 * Project: munch-core
 */
public interface DocumentStore {

    /**
     * @param place place to put to database
     */
    void put(Place place);

    /**
     * @param places list of place to put to database
     */
    void put(List<Place> places);

    /**
     * @param key of place to delete
     */
    boolean delete(String key);

    /**
     * @param keys list of key of place to delete
     */
    boolean delete(List<String> keys);
}
