package com.munch.struct.utils;

import com.munch.struct.Place;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:36 PM
 * Project: munch-core
 */
public interface DocumentDatabase {

    /**
     * @param place place to put to database
     */
    void put(Place place) throws Exception;

    /**
     * @param places list of place to put to database
     */
    void put(List<Place> places) throws Exception;

    /**
     * @param key of place to delete
     */
    boolean delete(String key) throws Exception;

    /**
     * @param keys list of key of place to delete
     */
    boolean delete(List<String> keys) throws Exception;

    /**
     * @param key key of place
     * @return Place with they key, nullable
     */
    Place get(String key) throws Exception;

    /**
     * @param keys list of key to query of a place
     * @return List of Place, non null values
     */
    List<Place> get(List<String> keys) throws Exception;
}
