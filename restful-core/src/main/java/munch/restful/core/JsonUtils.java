package munch.restful.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 16/6/2017
 * Time: 3:48 PM
 * Project: munch-core
 */
public final class JsonUtils {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toObject(JsonNode node, Class<T> clazz) throws IOException {
        return objectMapper.treeToValue(node, clazz);
    }

    public static <T> List<T> toList(JsonNode nodes, Class<T> clazz) throws IOException {
        CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.convertValue(nodes, type);
    }
}
