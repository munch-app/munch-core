package munch.search.elastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.restful.core.exception.JsonException;
import munch.search.data.Location;
import munch.search.data.Place;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 9/7/2017
 * Time: 1:00 AM
 * Project: munch-core
 */
@Singleton
public class ElasticMarshaller {
    private final ObjectMapper mapper;

    @Inject
    public ElasticMarshaller(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * If coordinates failed to parse, exception will be thrown
     *
     * @param location location to serialize to json
     * @return serialized json
     * @throws NullPointerException      if any points is null
     * @throws IndexOutOfBoundsException if points are not in the array
     * @throws NumberFormatException     if points are not double
     */
    public ObjectNode serialize(Location location) {
        ObjectNode node = mapper.createObjectNode();
        node.put("id", location.getId());
        node.put("name", location.getName());
        node.put("city", location.getCity());
        node.put("country", location.getCountry());
        node.put("center", location.getCenter());

        ObjectNode points = mapper.createObjectNode();
        points.put("type", "polygon");
        ArrayNode coordinates = mapper.createArrayNode();
        for (String point : location.getPoints()) {
            String[] split = point.split(",");
            double lat = Double.parseDouble(split[0].trim());
            double lng = Double.parseDouble(split[1].trim());
            coordinates.add(mapper.createArrayNode().add(lng).add(lat));
        }
        points.set("coordinates", coordinates);
        node.set("points", points);

        node.put("updatedDate", location.getUpdatedDate().getTime());

        // Suggest Field
        ArrayNode suggest = mapper.createArrayNode();
        suggest.add(location.getName());
        node.set("suggest", suggest);
        return node;
    }

    public ObjectNode serialize(Place place) {
        ObjectNode node = mapper.valueToTree(place);
        node.put("createdDate", place.getCreatedDate().getTime());
        node.put("updatedDate", place.getUpdatedDate().getTime());

        // Suggest Field
        ArrayNode suggest = mapper.createArrayNode();
        suggest.add(place.getName());
        node.set("suggest", suggest);
        return node;
    }

    /**
     * @param results results
     * @param <T>     deserialized type
     * @return deserialized type
     */
    public <T> List<T> deserializeList(JsonNode results) {
        List<T> list = new ArrayList<>();
        for (JsonNode result : results) list.add(deserialize(result));
        return list;
    }

    /**
     * @param node node to deserialize
     * @param <T>  deserialized type
     * @return deserialized type
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(JsonNode node) {
        switch (node.path("_type").asText()) {
            case "location":
                return (T) deserializeLocation(node);
            case "place":
                return (T) deserializePlace(node);
            default:
                return null;
        }
    }

    /**
     * @param node json node
     * @return deserialized Location
     */
    public Location deserializeLocation(JsonNode node) {
        Location location = new Location();
        location.setId(node.get("id").asText());
        location.setName(node.get("name").asText());
        location.setCity(node.get("city").asText());
        location.setCountry(node.get("country").asText());
        location.setCenter(node.get("center").asText());

        // points: { "type": "polygon", "coordinates": [[lng, lat]]}
        List<String> points = new ArrayList<>();
        for (JsonNode point : node.get("points").get("coordinates")) {
            points.add(point.get(1).asDouble() + "," + point.get(0).asDouble());
        }
        location.setPoints(points);
        location.setUpdatedDate(new Date(node.get("updatedDate").asLong()));
        return location;
    }

    /**
     * @param node json node
     * @return deserialized Place
     */
    public Place deserializePlace(JsonNode node) {
        try {
            return mapper.treeToValue(node, Place.class);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }
}
