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

    public TypedBlockMapper(Class<T> clazz, KeySupplier<T> keySupplier, BlockMapper blockMapper) {
        this.clazz = clazz;
        this.keySupplier = keySupplier;
        this.blockMapper = blockMapper;
    }

    public void save(T object) {
        blockMapper.save(keySupplier.getKey(object), object);
    }

    public T load(String key) {
        return blockMapper.load(clazz, key);
    }

    @FunctionalInterface
    public interface KeySupplier<T> {
        String getKey(T data);
    }
}
