package munch.api;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:17 PM
 * Project: munch-core
 */
public abstract class ObjectCleaner<T> {

    /**
     * @return class to clean
     */
    protected abstract Class<T> getClazz();

    /**
     * @param data to clean
     */
    protected abstract void clean(T data);
}
