package munch.places.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.restful.server.JsonUtils;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
public class SearchFilter {

    private static ObjectMapper mapper = JsonUtils.objectMapper;

    /**
     * @param shape node
     * @return JsonNode = {"geo_shape": { "location.geo": {...}}}
     */
    public static JsonNode createGeometryFilter(JsonNode shape) {
        ObjectNode locationGeo = mapper.createObjectNode();
        locationGeo.set("shape", shape);
        locationGeo.put("relation", "within");

        ObjectNode geoshape = mapper.createObjectNode();
        geoshape.set("location.geo", locationGeo);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("geo_shape", geoshape);
        return filter;
    }
}
