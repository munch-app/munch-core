package com.munch.struct.utils;

import com.munch.struct.Place;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:36 PM
 * Project: munch-core
 */
public interface SearchDatabase {

    /**
     * @param place place to put to search database
     */
    void put(Place place);

    /**
     * @param places list of place to put to search database
     */
    void put(List<Place> places);

    /**
     * @param key of place to delete
     */
    void delete(String key);

    /**
     * @param keys list of key of place to delete
     */
    void delete(List<String> keys);
}
