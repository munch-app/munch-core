package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by: Fuxing
 * Date: 16/6/18
 * Time: 6:11 PM
 * Project: munch-core
 */
public final class ElasticSuggestUtils {

    public static JsonNode make(String text, String latLng, int size) {
        ObjectNode completion = JsonUtils.createObjectNode()
                .put("field", "suggest")
                .put("fuzzy", true)
                .put("size", size);
        ObjectNode contexts = completion.putObject("contexts");

        // Context: LatLng
        if (StringUtils.isNotBlank(latLng)) {
            String[] lls = latLng.split(",");
            final double lat = Double.parseDouble(lls[0].trim());
            final double lng = Double.parseDouble(lls[1].trim());

            ArrayNode latLngArray = contexts.putArray("latLng");
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

            ArrayNode dataTypeArray = contexts.putArray("dataType");
            dataTypeArray.add("Place");
            dataTypeArray.add("Area");
            dataTypeArray.add("Tag");
        }
        ObjectNode root = JsonUtils.createObjectNode();
        root.putObject("suggest")
                .putObject("suggestions")
                .put("prefix", StringUtils.lowerCase(text))
                .set("completion", completion);

        // Query, parse and return options array node
        return root;
    }
}
