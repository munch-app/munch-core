package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.struct.Place;

import java.io.IOException;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
public interface SearchQuery {

    ObjectMapper mapper = new ObjectMapper();

    List<Place> query(JsonNode node) throws IOException;

    /**
     * @param lat    latitude
     * @param lng    longitude
     * @param radius radius in metre
     * @return JsonNode {"geo_shape": {}}
     */
    default JsonNode createGeoFilter(double lat, double lng, int radius) {
        ObjectNode shape = mapper.createObjectNode();
        shape.put("type", "circle");

        ArrayNode coordinates = mapper.createArrayNode();
        coordinates.add(lng);
        coordinates.add(lat);
        shape.set("coordinates", coordinates);
        shape.put("radius", radius + "m");

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
