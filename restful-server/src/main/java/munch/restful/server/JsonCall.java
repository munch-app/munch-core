package munch.restful.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.JsonException;
import munch.restful.core.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by: Fuxing
 * Date: 17/3/2017
 * Time: 1:23 AM
 * Project: munch-core
 */
public class JsonCall {
    private static final ObjectMapper objectMapper = JsonService.objectMapper;

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
     * @return request body as JsonNode
     * @throws JsonException json exception
     */
    public JsonNode bodyAsJson() {
        try {
            return objectMapper.readTree(request.bodyAsBytes());
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @return request body as json object
     */
    public <T> T bodyAsObject(Class<T> clazz) {
        try {
            return objectMapper.readValue(request.bodyAsBytes(), clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @param clazz clazz
     * @param <T>   Type
     * @return List as type
     */
    public <T> List<T> bodyAsList(Class<T> clazz) {
        return readJsonArray(bodyAsJson(), clazz);
    }

    /**
     * @param mapper json mapper
     * @param <T>    Type
     * @return List as type
     */
    public <T> List<T> bodyAsList(Function<JsonNode, T> mapper) {
        return readJsonArray(bodyAsJson(), mapper);
    }

    /**
     * Map json node to List of POJO Bean
     *
     * @param nodes  array node
     * @param mapper json mapper
     * @param <T>    Return Class Type
     * @return json body as @code{<T>}
     * @throws JsonException json exception
     */
    public static <T> List<T> readJsonArray(JsonNode nodes, Function<JsonNode, T> mapper) throws JsonException {
        List<T> list = new ArrayList<>();
        for (JsonNode node : nodes) {
            list.add(mapper.apply(node));
        }
        return list;
    }

    /**
     * Map json node to List of POJO Bean
     *
     * @param nodes array node
     * @param clazz Class Type to return
     * @param <T>   Return Class Type
     * @return json body as @code{<T>}
     * @throws JsonException json exception
     */
    public static <T> List<T> readJsonArray(JsonNode nodes, Class<T> clazz) throws JsonException {
        try {
            return JsonUtils.toList(nodes, clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @param name name of query string
     * @return long value from query string
     * @throws ParamException query param not found
     */
    public long queryLong(String name) throws ParamException {
        try {
            String value = queryString(name);
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ParamException(name);
        }
    }

    /**
     * @param name name of query string
     * @return integer value from query string
     * @throws ParamException query param not found
     */
    public int queryInt(String name) throws ParamException {
        try {
            String value = queryString(name);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ParamException(name);
        }
    }

    /**
     * @param name name of query string
     * @return double value from query string
     * @throws ParamException query param not found
     */
    public double queryDouble(String name) throws ParamException {
        try {
            String value = queryString(name);
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ParamException(name);
        }
    }

    /**
     * Boolean query string by checking string.equal("true")
     *
     * @param name name of query string
     * @return boolean value from query string
     * @throws ParamException query param not found
     */
    public boolean queryBool(String name) throws ParamException {
        return Boolean.parseBoolean(queryString(name));
    }

    /**
     * @param name name of query string
     * @return String value
     * @throws ParamException query param not found
     */
    public String queryString(String name) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        throw new ParamException(name);
    }

    /**
     * @param name         name of query string
     * @param defaultValue default String value
     * @return String value
     */
    public String queryString(String name, String defaultValue) throws ParamException {
        String value = request.queryParams(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        return defaultValue;
    }

    /**
     * @param name name of path param
     * @return Long value
     * @throws ParamException path param not found
     */
    public long pathLong(String name) throws ParamException {
        try {
            String value = pathString(name);
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ParamException(name);
        }
    }

    /**
     * @param name name of path param
     * @return Int value
     * @throws ParamException path param not found
     */
    public int pathInt(String name) throws ParamException {
        try {
            String value = pathString(name);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ParamException(name);
        }
    }

    /**
     * @param name name of path param
     * @return Double value
     * @throws ParamException path param not found
     */
    public double pathDouble(String name) throws ParamException {
        try {
            String value = pathString(name);
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ParamException(name);
        }
    }

    /**
     * @param name name of path param
     * @return String value
     * @throws ParamException path param not found
     */
    public String pathString(String name) throws ParamException {
        String value = request.params(name);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        throw new ParamException(name);
    }

    /**
     * @param name name of header
     * @return nullable string header
     */
    public String getHeader(String name) {
        return request().headers(name);
    }
}
