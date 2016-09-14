package com.munch.core.essential.json;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 12:47 AM
 * Project: struct
 */
public class BlockMapper {

    private PersistClient persistClient;
    private JsonConverter jsonConverter;

    public BlockMapper(PersistClient persistClient, JsonConverter jsonConverter) {
        this.persistClient = persistClient;
        this.jsonConverter = jsonConverter;
    }

    public void save(String key, Object object) {
        persistClient.save(key, jsonConverter.toJson(object));
    }

    public <T> T load(Class<T> clazz, String key) {
        return jsonConverter.fromJson(clazz, persistClient.load(key));
    }

}
