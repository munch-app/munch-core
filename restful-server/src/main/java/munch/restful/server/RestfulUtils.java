package munch.restful.server;

import munch.restful.server.exceptions.ParamException;
import munch.restful.server.exceptions.StructuredException;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Spark;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 3:13 PM
 * Project: munch-core
 */
public final class RestfulUtils {
    private RestfulUtils() {/* Utils Class */}

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
     * @param request spark request
     * @param name    name of query string
     * @return double value from query string
     * @throws ParamException query param not found
     */
    public static double queryDouble(Request request, String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Double.parseDouble(value);
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
     * @param request      spark request
     * @param name         name of query string
     * @param defaultValue default String value
     * @return String value
     */
    public static String queryString(Request request, String name, String defaultValue) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        return defaultValue;
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
     * @return Double value
     * @throws ParamException path param not found
     */
    public static double pathDouble(Request request, String name) throws ParamException {
        String value = request.params(name);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Double.parseDouble(value);
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

    /**
     * @return port of the server
     * @throws IllegalStateException when the server is not started
     */
    public static int getPort() {
        return Spark.port();
    }
}
