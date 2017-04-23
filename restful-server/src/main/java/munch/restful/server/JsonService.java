package munch.restful.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.restful.server.exceptions.JsonException;
import spark.ResponseTransformer;
import spark.RouteGroup;
import spark.Spark;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:29 PM
 * Project: munch-core
 */
public interface JsonService extends RestfulService {
    String APP_JSON = "application/json";

    ObjectMapper objectMapper = JsonUtils.objectMapper;

    /**
     * Theses nodes are pre-build notes, use the as a whole don't
     * put them in another node
     */
    JsonNode Meta200 = JsonUtils.nodes(JsonUtils.metaNode(200));
    JsonNode Meta404 = JsonUtils.nodes(JsonUtils.metaNode(
            404, "ObjectNotFound", "Object requested not found."));

    String Meta200String = JsonUtils.toJson(Meta200);
    String Meta404String = JsonUtils.toJson(Meta404);

    ResponseTransformer toJson = (Object model) -> {
        // Check if JsonNode is any of the static helpers
        if (model == null || model == Meta404) return Meta404String;
        if (model == Meta200) return Meta200String;

        // Json node means already structured
        if (model instanceof JsonNode) return JsonUtils.toJson(model);

        // If not json node, wrap it into data node
        return JsonUtils.toJson(JsonUtils.nodes(200, model));
    };

    /**
     * Override for custom transformer
     *
     * @return default toJson transformer for json service to use
     */
    default ResponseTransformer toJson() {
        return toJson;
    }

    /**
     * @param path       path to add prefix to route
     * @param routeGroup route
     */
    default void PATH(String path, RouteGroup routeGroup) {
        Spark.path(path, routeGroup);
    }

    /**
     * Map route for HTTP Get
     *
     * @param path  the path
     * @param route json route
     */
    default void GET(String path, JsonRoute route) {
        Spark.get(path, route, toJson());
    }

    /**
     * Map route for HTTP Post
     *
     * @param path  the path
     * @param route json route
     */
    default void POST(String path, JsonRoute route) {
        Spark.post(path, route, toJson());
    }

    /**
     * Map route for HTTP Post
     *
     * @param path  the path
     * @param route json node route
     */
    default void POST(String path, JsonRoute.Node route) {
        Spark.post(path, route, toJson());
    }

    /**
     * Map route for HTTP Put
     *
     * @param path  the path
     * @param route json route
     */
    default void PUT(String path, JsonRoute route) {
        Spark.put(path, route, toJson());
    }

    /**
     * Map route for HTTP Put
     *
     * @param path  the path
     * @param route json node route
     */
    default void PUT(String path, JsonRoute.Node route) {
        Spark.put(path, route, toJson());
    }

    /**
     * Map route for HTTP Delete
     *
     * @param path  the path
     * @param route json route
     */
    default void DELETE(String path, JsonRoute route) {
        Spark.delete(path, route, toJson());
    }

    /**
     * Map route for HTTP Delete
     *
     * @param path  the path
     * @param route json node route
     */
    default void DELETE(String path, JsonRoute.Node route) {
        Spark.delete(path, route, toJson());
    }

    /**
     * Map route for HTTP Head
     *
     * @param path  the path
     * @param route json route
     */
    default void HEAD(String path, JsonRoute route) {
        Spark.head(path, route, toJson());
    }

    /**
     * Map route for HTTP Patch
     *
     * @param path  the path
     * @param route json route
     */
    default void PATCH(String path, JsonRoute route) {
        Spark.patch(path, route, toJson());
    }

    /**
     * {@link JsonUtils#readJson(JsonNode, Class)}
     */
    default <T> T readObject(JsonNode node, Class<T> clazz) throws JsonException {
        return JsonUtils.readJson(node, clazz);
    }

    /**
     * {@link JsonUtils#toTree(Object)}
     */
    default JsonNode toTree(Object object) {
        return JsonUtils.toTree(object);
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
