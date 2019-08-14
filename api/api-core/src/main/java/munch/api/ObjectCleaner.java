package munch.api;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:17 PM
 * Project: munch-core
 */
@Deprecated
public abstract class ObjectCleaner<T> {

    /**
     * @return class of data to clean
     */
    protected abstract Class<T> getClazz();

    /**
     * @param data to clean
     */
    protected abstract void clean(T data);
}
