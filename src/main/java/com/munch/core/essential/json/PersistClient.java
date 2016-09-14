package com.munch.core.essential.json;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 1:11 AM
 * Project: essential
 */
public interface PersistClient {

    void save(String key, String content);

    String load(String key);

}
