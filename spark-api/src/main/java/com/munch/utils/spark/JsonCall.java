package com.munch.utils.spark;

import com.fasterxml.jackson.databind.JsonNode;
import com.munch.utils.spark.exceptions.JsonException;
import com.munch.utils.spark.exceptions.ParamException;
import spark.Request;
import spark.Response;

/**
 * Created by: Fuxing
 * Date: 17/3/2017
 * Time: 1:23 AM
 * Project: munch-core
 */
public class JsonCall {

    private final Request request;
    private final Response response;

    /**
     * @param request  spark request
     * @param response spark response
     */
    JsonCall(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    /**
     * @return Spark request
     */
    public Request request() {
        return request;
    }

    /**
     * @return Spark response
     */
    public Response response() {
        return response;
    }

    /**
     * @return request body as json node
     */
    public JsonNode bodyAsJson() {
        return JsonUtils.readJson(request);
    }

    /**
     * {@link JsonUtils#readJson(JsonNode, Class)}
     */
    public <T> T readJson(JsonNode node, Class<T> clazz) throws JsonException {
        return JsonUtils.readJson(node, clazz);
    }

    /**
     * {@link SparkUtils#queryLong(Request, String)}
     */
    public long queryLong(String name) throws ParamException {
        return SparkUtils.queryLong(request, name);
    }

    /**
     * {@link SparkUtils#queryInt(Request, String)}
     */
    public int queryInt(String name) throws ParamException {
        return SparkUtils.queryInt(request, name);
    }

    /**
     * {@link SparkUtils#queryBool(Request, String)}
     */
    public boolean queryBool(String name) throws ParamException {
        return SparkUtils.queryBool(request, name);
    }

    /**
     * {@link SparkUtils#queryString(Request, String)}
     */
    public String queryString(String name) throws ParamException {
        return SparkUtils.queryString(request, name);
    }

    /**
     * {@link SparkUtils#pathLong(Request, String)}
     */
    public long pathLong(String name) throws ParamException {
        return SparkUtils.pathLong(request, name);
    }

    /**
     * {@link SparkUtils#pathInt(Request, String)}
     */
    public int pathInt(String name) throws ParamException {
        return SparkUtils.pathInt(request, name);
    }

    /**
     * {@link SparkUtils#pathString(Request, String)}
     */
    public String pathString(String name) throws ParamException {
        return SparkUtils.pathString(request, name);
    }
}
