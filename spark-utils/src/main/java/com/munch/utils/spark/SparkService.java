package com.munch.utils.spark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munch.utils.spark.exceptions.JsonException;
import spark.Request;
import spark.ResponseTransformer;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:29 PM
 * Project: munch-core
 */
public interface SparkService extends SparkRouter {
    String APP_JSON = "application/json";

    ObjectMapper objectMapper = SparkUtils.objectMapper;
    ResponseTransformer toJson = objectMapper::writeValueAsString;

    /**
     * Throws a expected error
     *
     * @param message message to put to response body
     * @param status  status code to response
     */
    default void throwsMessage(String message, int status) {
        SparkUtils.throwsMessage(message, status);
    }

    /**
     * @param request spark request
     * @return JsonNode
     * @throws JsonException json exception from reading
     */
    default JsonNode readNode(Request request) throws JsonException {
        return SparkUtils.readNode(request);
    }

    /**
     * @param request spark request
     * @param clazz   class to return
     * @param <T>     Class Type
     * @return JsonNode
     * @throws JsonException expected json exception
     */
    default <T> T readJson(Request request, Class<T> clazz) throws JsonException {
        return SparkUtils.readJson(request, clazz);
    }
}
