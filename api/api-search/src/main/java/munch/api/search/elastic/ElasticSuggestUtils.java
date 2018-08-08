package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

/**
 * Created by: Fuxing
 * Date: 16/6/18
 * Time: 6:11 PM
 * Project: munch-core
 */
public final class ElasticSuggestUtils {

    public static JsonNode make(String text, @Nullable String latLng, int size) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.putObject("suggest")
                .putObject("suggestions")
                .put("prefix", StringUtils.lowerCase(text))
                .set("completion", makeCompletion(latLng, size));
        return root;
    }

    private static JsonNode makeCompletion(@Nullable String latLng, int size) {
        ObjectNode completion = JsonUtils.createObjectNode();
        completion.put("field", "suggest");
        completion.put("fuzzy", true);
        completion.put("size", size);
        completion.set("contexts", makeContexts(latLng));
        return completion;
    }

    private static JsonNode makeContexts(String latLng) {
        ObjectNode contexts = JsonUtils.createObjectNode();
        contexts.set("dataType", makeDataType());

        if (StringUtils.isNotBlank(latLng)) {
            contexts.set("latLng", makeLatLng(latLng));
        }
        return contexts;
    }

    private static JsonNode makeDataType() {
        ArrayNode arrayNode = JsonUtils.createArrayNode();
        arrayNode.addObject()
                .put("context", "Area")
                .put("boost", 3);

        arrayNode.addObject()
                .put("context", "Tag")
                .put("boost", 2);

        arrayNode.addObject()
                .put("context", "Place");
        return arrayNode;
    }

    private static JsonNode makeLatLng(String latLng) {
        String[] lls = latLng.split(",");
        final double lat = Double.parseDouble(lls[0].trim());
        final double lng = Double.parseDouble(lls[1].trim());

        ArrayNode latLngArray = JsonUtils.createArrayNode();
        latLngArray.addObject()
                .put("precision", 3)
                .putObject("context")
                .put("lat", lat)
                .put("lon", lng);

        latLngArray.addObject()
                .put("precision", 6)
                .put("boost", 1.05)
                .putObject("context")
                .put("lat", lat)
                .put("lon", lng);
        return latLngArray;
    }
}
