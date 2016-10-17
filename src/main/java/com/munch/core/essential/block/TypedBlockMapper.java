package com.munch.core.essential.block;

/**
 * Created by: Fuxing
 * Date: 24/9/2016
 * Time: 5:45 PM
 * Project: essential
 */
public class TypedBlockMapper<T> {

    protected final Class<T> clazz;
    protected final KeySupplier<T> keySupplier;
    protected final BlockMapper blockMapper;

    /**
     * Typed Block Mapper that assist in the type key mapping
     * With this mapper, key is not required for each action but key supplier is required to load and save object
     *
     * @param clazz       class of the object
     * @param keySupplier key supplier function to find the key of the object
     * @param blockMapper block mapper to use for this typed block mapper
     */
    public TypedBlockMapper(Class<T> clazz, KeySupplier<T> keySupplier, BlockMapper blockMapper) {
        this.clazz = clazz;
        this.keySupplier = keySupplier;
        this.blockMapper = blockMapper;
    }

    /**
     * @param object block object to save
     */
    public void save(T object) {
        blockMapper.save(keySupplier.getKey(object), object);
    }

    /**
     * @param key key of object
     * @return object with the given key
     */
    public T load(String key) {
        return blockMapper.load(clazz, key);
    }

    @FunctionalInterface
    public interface KeySupplier<T> {
        String getKey(T data);
    }
}
