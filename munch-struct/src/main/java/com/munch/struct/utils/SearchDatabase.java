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
    void put(Place place) throws Exception;

    /**
     * @param places list of place to put to search database
     */
    void put(List<Place> places) throws Exception;

    /**
     * @param key of place to delete
     */
    void delete(String key) throws Exception;

    /**
     * @param keys list of key of place to delete
     */
    void delete(List<String> keys) throws Exception;
}
