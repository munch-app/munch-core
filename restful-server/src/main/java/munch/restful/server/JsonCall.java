package munch.restful.server;

import com.fasterxml.jackson.databind.JsonNode;
import munch.restful.server.exceptions.ParamException;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

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
     * @return request body as json object
     */
    public <T> T bodyAsObject(Class<T> clazz) {
        return JsonUtils.readJson(request, clazz);
    }

    /**
     * @param clazz clazz
     * @param <T>   Type
     * @return List as type
     */
    public <T> List<T> bodyAsList(Class<T> clazz) {
        JsonNode nodes = bodyAsJson();
        List<T> list = new ArrayList<>();
        for (JsonNode node : nodes) {
            list.add(JsonUtils.readJson(node, clazz));
        }
        return list;
    }

    /**
     * {@link RestfulUtils#queryLong(Request, String)}
     */
    public long queryLong(String name) throws ParamException {
        return RestfulUtils.queryLong(request, name);
    }

    /**
     * {@link RestfulUtils#queryInt(Request, String)}
     */
    public int queryInt(String name) throws ParamException {
        return RestfulUtils.queryInt(request, name);
    }

    /**
     * {@link RestfulUtils#queryBool(Request, String)}
     */
    public boolean queryBool(String name) throws ParamException {
        return RestfulUtils.queryBool(request, name);
    }

    /**
     * {@link RestfulUtils#queryString(Request, String)}
     */
    public String queryString(String name) throws ParamException {
        return RestfulUtils.queryString(request, name);
    }

    /**
     * {@link RestfulUtils#pathLong(Request, String)}
     */
    public long pathLong(String name) throws ParamException {
        return RestfulUtils.pathLong(request, name);
    }

    /**
     * {@link RestfulUtils#pathInt(Request, String)}
     */
    public int pathInt(String name) throws ParamException {
        return RestfulUtils.pathInt(request, name);
    }

    /**
     * {@link RestfulUtils#pathString(Request, String)}
     */
    public String pathString(String name) throws ParamException {
        return RestfulUtils.pathString(request, name);
    }
}
