package com.munch.utils.spark;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:57 PM
 * Project: munch-core
 */
public interface SparkRouter {

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
     * {@link SparkUtils#throwError(String, String)}
     */
    default void throwError(String type, String message) {
        SparkUtils.throwError(type, message);
    }
}
