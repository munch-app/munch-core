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
     */
    default void start() {
        route();
    }

    /**
     * Wire all the routes
     */
    void route();

    /**
     * @param request spark request
     * @param name    name of query string
     * @return Long value
     * @throws ParamException if parameter don't exist
     */
    default long queryLong(Request request, String name) throws ParamException {
        return SparkUtils.queryLong(request, name);
    }

    /**
     * @param request spark request
     * @param name    name of query string
     * @return Int value
     * @throws ParamException if parameter don't exist
     */
    default int queryInt(Request request, String name) throws ParamException {
        return SparkUtils.queryInt(request, name);
    }

    /**
     * string.equal("true")
     *
     * @param request spark request
     * @param name    name of query string
     * @return Boolean value
     * @throws ParamException if parameter don't exist
     */
    default boolean queryBool(Request request, String name) throws ParamException {
        return SparkUtils.queryBool(request, name);
    }

    /**
     * @param request spark request
     * @param name    name of query string
     * @return String value
     * @throws ParamException if parameter don't exist
     */
    default String queryString(Request request, String name) throws ParamException {
        return SparkUtils.queryString(request, name);
    }
}
