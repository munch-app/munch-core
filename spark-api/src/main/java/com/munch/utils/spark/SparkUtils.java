package com.munch.utils.spark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munch.utils.spark.exceptions.JsonException;
import com.munch.utils.spark.exceptions.ParamException;
import com.munch.utils.spark.exceptions.StructuredException;
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
     * Construct a error to throw.
     * Spark Server will catch this error and present the error in meta.
     * E.g.
     * <pre>
     * {
     *     meta: {
     *         code: 400,
     *         type: "PascalCase"
     *         message: "English sentence."
     *     }
     * }
     * </pre>
     *
     * @param type    error type, short hand for consumer to identify and present their own response
     *                String should be formatted in PascalCase
     * @param message error message, in full readable english sentence for consumer to present
     *                String should be formatted in structured english.
     */
    public static void throwError(String type, String message) {
        throw new StructuredException(type, message);
    }

    /**
     * @param params list of param that is not available
     * @throws ParamException param exception
     */
    public static void throwParams(String... params) throws ParamException {
        throw new ParamException(params);
    }

    /**
     * @param request spark request
     * @return request body as JsonNode
     * @throws JsonException json exception
     */
    public static JsonNode readJson(Request request) throws JsonException {
        try {
            return objectMapper.readTree(request.bodyAsBytes());
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @param request spark request
     * @param clazz   Class Type to return
     * @param <T>     Return Class Type
     * @return request body as @code{<T>}
     * @throws JsonException json exception
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
     * @return long value from query string
     * @throws ParamException query param not found
     */
    public static long queryLong(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ParamException(name);
            }
        }
        throw new ParamException(name);
    }

    /**
     * @param request spark request
     * @param name    name of query string
     * @return integer value from query string
     * @throws ParamException query param not found
     */
    public static int queryInt(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ParamException(name);
            }
        }
        throw new ParamException(name);
    }

    /**
     * Boolean query string by checking string.equal("true")
     *
     * @param request spark request
     * @param name    name of query string
     * @return boolean value from query string
     * @throws ParamException query param not found
     */
    public static boolean queryBool(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.parseBoolean(value);
        }
        throw new ParamException(name);
    }

    /**
     * @param request spark request
     * @param name    name of query string
     * @return String value
     * @throws ParamException query param not found
     */
    public static String queryString(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        throw new ParamException(name);
    }

    /**
     * @param request spark request
     * @param name    name of path param
     * @return Long value
     * @throws ParamException path param not found
     */
    public static long pathLong(Request request, String name) throws ParamException {
        String value = request.params(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new ParamException(name);
            }
        }
        throw new ParamException(name);
    }

    /**
     * @param request spark request
     * @param name    name of path param
     * @return Int value
     * @throws ParamException path param not found
     */
    public static int pathInt(Request request, String name) throws ParamException {
        String value = request.params(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ParamException(name);
            }
        }
        throw new ParamException(name);
    }

    /**
     * @param request spark request
     * @param name    name of path param
     * @return String value
     * @throws ParamException path param not found
     */
    public static String pathString(Request request, String name) throws ParamException {
        String value = request.params(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        throw new ParamException(name);
    }

}
