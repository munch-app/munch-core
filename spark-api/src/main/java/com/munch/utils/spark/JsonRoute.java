package com.munch.utils.spark;

import com.fasterxml.jackson.databind.JsonNode;
import spark.Request;
import spark.Response;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 4:22 PM
 * Project: munch-core
 */
@FunctionalInterface
public interface JsonRoute {

    /**
     * Invoked when a request is made on this route's corresponding path e.g. '/hello'
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * @param node     The Json object in the body
     * @return The content to be set in the response
     * @throws java.lang.Exception implementation can choose to throw exception
     */
    Object handle(Request request, Response response, JsonNode node) throws Exception;

}