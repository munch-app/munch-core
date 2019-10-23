package app.munch.model;

import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;

/**
 * @author Fuxing Loh
 * @since 2019-10-23 at 10:27
 */
public interface Lazy {

    /**
     * @param value        to load/initialize
     * @param defaultValue if LazyInitializationException
     * @param <T>          type of value
     * @return loaded or default
     */
    static <T> T load(T value, T defaultValue) {
        try {
            Hibernate.initialize(value);
            return value;
        } catch (LazyInitializationException e) {
            return defaultValue;
        }
    }
}
