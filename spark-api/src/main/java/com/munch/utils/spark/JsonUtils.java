package com.munch.utils.spark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.munch.utils.spark.exceptions.JsonException;
import spark.Request;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 4:00 PM
 * Project: munch-core
 */
public final class JsonUtils {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @return new object node for consumption
     */
    public static ObjectNode newNode() {
        return objectMapper.createObjectNode();
    }

    /**
     * @return new array node for consumption
     */
    public static ArrayNode newArrayNode() {
        return objectMapper.createArrayNode();
    }

    /**
     * @param meta meta node
     * @return object node with meta only
     */
    public static ObjectNode nodes(JsonNode meta) {
        ObjectNode nodes = newNode();
        nodes.set("meta", meta);
        return nodes;
    }

    /**
     * @param meta meta node
     * @param data data node
     * @return object node with meta and data node
     */
    public static ObjectNode nodes(JsonNode meta, JsonNode data) {
        ObjectNode nodes = newNode();
        nodes.set("meta", meta);
        nodes.set("data", data);
        return nodes;
    }

    /**
     * @param meta   meta node
     * @param object object
     * @return object node with meta and data node
     */
    public static ObjectNode nodes(JsonNode meta, Object object) {
        return nodes(meta, objectMapper.valueToTree(object));
    }

    /**
     * @param code code for meta node
     * @param data data node
     * @return object node with meta and data node
     */
    public static ObjectNode nodes(int code, JsonNode data) {
        ObjectNode nodes = newNode();
        nodes.set("meta", metaNode(code));
        nodes.set("data", data);
        return nodes;
    }

    /**
     * @param code   code for meta node
     * @param object object
     * @return object node with meta and data node
     */
    public static ObjectNode nodes(int code, Object object) {
        return nodes(code, objectMapper.valueToTree(object));
    }

    /**
     * @param code         status code
     * @param errorType    error type in pascal case
     * @param errorMessage error message in english sentence.
     * @return new meta node with status code, error type and message
     */
    public static ObjectNode metaNode(int code, String errorType, String errorMessage) {
        ObjectNode metaNode = objectMapper.createObjectNode();
        return metaNode.put("code", code).put("errorType", errorType).put("errorMessage", errorMessage);
    }

    /**
     * @param code status code
     * @return new meta node with status code only
     */
    public static ObjectNode metaNode(int code) {
        ObjectNode metaNode = objectMapper.createObjectNode();
        return metaNode.put("code", code);
    }

    /**
     * @param object object
     * @return object write to json string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
     * Read json node to POJO Bean
     *
     * @param node  json node
     * @param clazz Class Type to return
     * @param <T>   Return Class Type
     * @return json body as @code{<T>}
     * @throws JsonException json exception
     */
    public static <T> T readJson(JsonNode node, Class<T> clazz) throws JsonException {
        try {
            return objectMapper.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }
}
