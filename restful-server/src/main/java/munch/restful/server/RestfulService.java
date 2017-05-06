package munch.restful.server;

import munch.restful.server.exceptions.ParamException;
import org.apache.commons.lang3.StringUtils;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:57 PM
 * Project: munch-core
 */
public interface RestfulService {

    /**
     * Start the router
     * By wiring all the routes
     */
    default void start() {
        route();
    }

    /**
     * Wire all the routes
     */
    void route();

    /**
     * {@link RestfulUtils#throwError(String, String)}
     */
    default void throwError(String type, String message) {
        RestfulUtils.throwError(type, message);
    }

    /**
     * {@link RestfulUtils#throwError(String, String, int)}
     */
    default void throwError(String type, String message, int code) {
        RestfulUtils.throwError(type, message, code);
    }

    /**
     * @param key   key to param
     * @param value value that is required non null
     * @return same value
     * @throws ParamException if null
     */
    default <T> T requireNonNull(String key, T value) throws ParamException {
        if (value == null) throw new ParamException(key);
        return value;
    }

    /**
     * @param key   key to param
     * @param value value that is required non blank
     * @return same value
     * @throws ParamException if blank
     */
    default <T extends CharSequence> T requireNonBlank(String key, T value) throws ParamException {
        if (StringUtils.isBlank(value)) throw new ParamException(key);
        return value;
    }
}
