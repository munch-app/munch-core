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
public interface JsonService extends SparkRouter {
    String APP_JSON = "application/json";

    // TODO special json service

    ObjectMapper objectMapper = SparkUtils.objectMapper;
    ResponseTransformer toJson = objectMapper::writeValueAsString;

    /**
     * {@link SparkUtils#readJson(Request)}
     */
    default JsonNode readJson(Request request) throws JsonException {
        return SparkUtils.readJson(request);
    }

    /**
     * {@link SparkUtils#readJson(Request, Class)}
     */
    default <T> T readJson(Request request, Class<T> clazz) throws JsonException {
        return SparkUtils.readJson(request, clazz);
    }

}
