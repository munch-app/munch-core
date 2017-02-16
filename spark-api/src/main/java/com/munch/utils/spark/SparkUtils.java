package com.munch.utils.spark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munch.utils.spark.exceptions.ExpectedError;
import com.munch.utils.spark.exceptions.JsonException;
import com.munch.utils.spark.exceptions.ParamException;
import org.apache.commons.lang3.StringUtils;
import spark.Request;

import java.io.IOException;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 3:13 PM
 * Project: munch-core
 */
public final class SparkUtils {
    private SparkUtils() {/* Utils Class */}

    public static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Throws a expected error
     *
     * @param message message to put to response body
     * @param status  status code to response
     */
    public static void throwsMessage(String message, int status) {
        throw new ExpectedError(message, status);
    }

    /**
     * @param request spark request
     * @return JsonNode
     * @throws JsonException expected json exception
     */
    public static JsonNode readNode(Request request) throws JsonException {
        try {
            return objectMapper.readTree(request.bodyAsBytes());
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @param request spark request
     * @param clazz   class to return
     * @param <T>     Class Type
     * @return JsonNode
     * @throws JsonException expected json exception
     */
    public static <T> T readJson(Request request, Class<T> clazz) throws JsonException {
        try {
            return objectMapper.readValue(request.bodyAsBytes(), clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @param request spark request
     * @param name    name of query string
     * @return Long value
     * @throws ParamException if parameter don't exist
     */
    public static long queryLong(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ParamException(name, e.getMessage());
            }
        }
        throw new ParamException(name, "param is blank");
    }

    /**
     * @param request spark request
     * @param name    name of query string
     * @return Int value
     * @throws ParamException if parameter don't exist
     */
    public static int queryInt(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ParamException(name, e.getMessage());
            }
        }
        throw new ParamException(name, "param is blank");
    }

    /**
     * string.equal("true")
     *
     * @param request spark request
     * @param name    name of query string
     * @return Boolean value
     * @throws ParamException if parameter don't exist
     */
    public static boolean queryBool(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.parseBoolean(value);
        }
        throw new ParamException(name, "param is blank");
    }

    /**
     * @param request spark request
     * @param name    name of query string
     * @return String value
     * @throws ParamException if parameter don't exist
     */
    public static String queryString(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        throw new ParamException(name, "param is blank");
    }

}
