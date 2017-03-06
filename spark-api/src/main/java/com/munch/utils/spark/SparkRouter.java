package com.munch.utils.spark;

import com.munch.utils.spark.exceptions.ParamException;
import spark.Request;

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

    /**
     * {@link SparkUtils#queryLong(Request, String)}
     */
    default long queryLong(Request request, String name) throws ParamException {
        return SparkUtils.queryLong(request, name);
    }

    /**
     * {@link SparkUtils#queryInt(Request, String)}
     */
    default int queryInt(Request request, String name) throws ParamException {
        return SparkUtils.queryInt(request, name);
    }

    /**
     * {@link SparkUtils#queryBool(Request, String)}
     */
    default boolean queryBool(Request request, String name) throws ParamException {
        return SparkUtils.queryBool(request, name);
    }

    /**
     * {@link SparkUtils#queryString(Request, String)}
     */
    default String queryString(Request request, String name) throws ParamException {
        return SparkUtils.queryString(request, name);
    }

    /**
     * {@link SparkUtils#pathLong(Request, String)}
     */
    default long pathLong(Request request, String name) throws ParamException {
        return SparkUtils.pathLong(request, name);
    }

    /**
     * {@link SparkUtils#pathInt(Request, String)}
     */
    default int pathInt(Request request, String name) throws ParamException {
        return SparkUtils.pathInt(request, name);
    }

    /**
     * {@link SparkUtils#pathString(Request, String)}
     */
    default String pathString(Request request, String name) throws ParamException {
        return SparkUtils.pathString(request, name);
    }
}
