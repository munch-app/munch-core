package munch.search.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2/7/2017
 * Time: 3:12 AM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Location {
    private String id;

    private String name;
    private String city;
    private String country;

    private String center;
    private List<String> points;

    private int sort;
    private Date updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return center as "lat,lng"
     */
    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Singleton
    public static class Marshaller {
        private final ObjectMapper mapper;

        @Inject
        public Marshaller(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        /**
         * @param node json node
         * @return deserialized Location
         */
        public Location deserialize(JsonNode node) {
            Location location = new Location();
            location.setId(node.get("id").asText());
            location.setName(node.get("name").asText());
            location.setCity(node.get("city").asText());
            location.setCountry(node.get("country").asText());
            location.setCenter(node.get("center").asText());

            // points: {type: "polygon", "coordinates": [[lng, lat]]}
            List<String> points = new ArrayList<>();
            for (JsonNode point : node.get("points").get("coordinates")) {
                points.add(point.get(1).asDouble() + "," + point.get(0).asDouble());
            }
            location.setPoints(points);
            location.setSort(node.get("sort").asInt());
            location.setUpdatedDate(new Date(node.get("updatedDate").asLong()));
            return location;
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

            node.put("sort", location.getSort());
            node.put("updatedDate", location.getUpdatedDate().getTime());
            return node;
        }
    }
}
