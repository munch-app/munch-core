package com.munch.core.essential.block;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 12:47 AM
 * Project: struct
 */
public class BlockMapper {

    protected PersistClient persistClient;
    protected JsonConverter jsonConverter;

    /**
     * @param persistClient persist client to use to persist the object
     * @param jsonConverter default json converter to use for the object
     */
    public BlockMapper(PersistClient persistClient, JsonConverter jsonConverter) {
        this.persistClient = persistClient;
        this.jsonConverter = jsonConverter;
    }

    /**
     * Save Block with a key
     *
     * @param key    unique key of the object
     * @param object the java object
     */
    public void save(String key, Object object) {
        persistClient.save(key, jsonConverter.toJson(object));
    }

    /**
     * Load the java object
     *
     * @param clazz class type of the objec
     * @param key   key of the object
     * @param <T>   Class of Object
     * @return Object from persist store
     */
    public <T> T load(Class<T> clazz, String key) {
        return jsonConverter.fromJson(clazz, persistClient.load(key));
    }

}
