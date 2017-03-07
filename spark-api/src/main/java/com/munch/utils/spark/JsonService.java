package com.munch.utils.spark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.munch.utils.spark.exceptions.JsonException;
import spark.ResponseTransformer;
import spark.Route;
import spark.RouteGroup;
import spark.Spark;

import static com.munch.utils.spark.JsonUtils.metaNode;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:29 PM
 * Project: munch-core
 */
public interface JsonService extends SparkRouter {
    String APP_JSON = "application/json";

    JsonNode Meta200 = JsonUtils.nodes(metaNode(200));
    JsonNode Meta404 = JsonUtils.nodes(metaNode(404, "ObjectNotFound",
            "Object requested not found."));

    ObjectMapper objectMapper = new ObjectMapper();
    ResponseTransformer toJson = (Object model) -> {
        // Object not found
        if (model == null) {
            return "{\"meta\":{\"code\":404,\"errorType\":\"ObjectNotFound\",\"errorMessage\":\"Object requested not found.\"}}";
        }

        // Json node means already structured
        if (model instanceof JsonNode) {
            // Check if JsonNode is any of the static helpers
            if (model == Meta200) {
                return "{\"meta\":{\"code\":200}}";
            } else if (model == Meta404) {
                return "{\"meta\":{\"code\":404,\"errorType\":\"ObjectNotFound\",\"errorMessage\":\"Object requested not found.\"}}";
            } else {
                return objectMapper.writeValueAsString(model);
            }
        }

        // If not json node, wrap it into data node
        JsonNode nodes = JsonUtils.nodes(200, model);
        return objectMapper.writeValueAsString(nodes);
    };

    /**
     * @param path       path to add prefix to route
     * @param routeGroup route
     */
    default void path(String path, RouteGroup routeGroup) {
        Spark.path(path, routeGroup);
    }

    /**
     * Map route for HTTP Get
     *
     * @param path  the path
     * @param route the route
     */
    default void get(String path, Route route) {
        Spark.get(path, route, toJson);
    }

    /**
     * Map route for HTTP Post
     *
     * @param path  the path
     * @param route the route
     */
    default void post(String path, Route route) {
        Spark.post(path, route, toJson);
    }

    /**
     * Map route for HTTP Post
     * This method provide json node in route function
     *
     * @param path  the path
     * @param route the json route
     */
    default void post(String path, JsonRoute route) {
        Spark.post(path, (request, response) ->
                route.handle(request, response, JsonUtils.readJson(request)), toJson);
    }

    /**
     * Map route for HTTP Put
     *
     * @param path  the path
     * @param route the route
     */
    default void put(String path, Route route) {
        Spark.put(path, route, toJson);
    }

    /**
     * Map route for HTTP Put
     * This method provide json node in route function
     *
     * @param path  the path
     * @param route the json route
     */
    default void put(String path, JsonRoute route) {
        Spark.put(path, (request, response) ->
                route.handle(request, response, JsonUtils.readJson(request)), toJson);
    }

    /**
     * Map route for HTTP Delete
     *
     * @param path  the path
     * @param route the route
     */
    default void delete(String path, Route route) {
        Spark.delete(path, route, toJson);
    }

    /**
     * Map route for HTTP Head
     *
     * @param path  the path
     * @param route the route
     */
    default void head(String path, Route route) {
        Spark.head(path, route, toJson);
    }

    /**
     * Map route for HTTP Patch
     *
     * @param path  the path
     * @param route the route
     */
    default void patch(String path, Route route) {
        Spark.patch(path, route, toJson);
    }

    /**
     * {@link JsonUtils#readJson(JsonNode, Class)}
     */
    default <T> T readJson(JsonNode node, Class<T> clazz) throws JsonException {
        return JsonUtils.readJson(node, clazz);
    }

    /**
     * {@link JsonUtils#newNode()}
     */
    default ObjectNode newNode() {
        return JsonUtils.newNode();
    }

    /**
     * {@link JsonUtils#newArrayNode()}
     */
    default ArrayNode newArrayNode() {
        return JsonUtils.newArrayNode();
    }

    /**
     * {@link JsonUtils#nodes(JsonNode)}
     */
    default ObjectNode nodes(JsonNode meta) {
        return JsonUtils.nodes(meta);
    }

    /**
     * {@link JsonUtils#nodes(JsonNode, JsonNode)}
     */
    default ObjectNode nodes(JsonNode meta, JsonNode data) {
        return JsonUtils.nodes(meta, data);
    }

    /**
     * {@link JsonUtils#nodes(JsonNode, Object)}
     */
    default ObjectNode nodes(JsonNode meta, Object object) {
        return JsonUtils.nodes(meta, object);
    }

    /**
     * {@link JsonUtils#nodes(int, JsonNode)}
     */
    default ObjectNode nodes(int code, JsonNode data) {
        return JsonUtils.nodes(code, data);
    }

    /**
     * {@link JsonUtils#nodes(int, Object)}
     */
    default ObjectNode nodes(int code, Object object) {
        return JsonUtils.nodes(code, object);
    }
}
