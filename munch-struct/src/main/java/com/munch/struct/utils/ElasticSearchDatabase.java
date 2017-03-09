package com.munch.struct.utils;

import com.google.inject.Singleton;
import com.munch.struct.Place;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:27 PM
 * Project: munch-core
 */
@Singleton
public class ElasticSearchDatabase implements SearchDatabase {
    // https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html

    @Override
    public void put(Place place) {

    }

    @Override
    public void put(List<Place> places) {

    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void delete(List<String> keys) {

    }
}
