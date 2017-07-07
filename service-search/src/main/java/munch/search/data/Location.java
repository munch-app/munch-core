package munch.search.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public static class Deserializer extends StdDeserializer<Location> {

        protected Deserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Location deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            Location location = new Location();
            location.setId(node.get("id").asText());
            location.setName(node.get("name").asText());
            location.setCity(node.get("city").asText());
            location.setCountry(node.get("country").asText());

            location.setCenter(node.get("center").asText());
            // TODO points: {type: "polygon", "coordinates": [[lng, lat]]}
            location.setUpdatedDate(new Date(node.get("updatedDate").asLong()));
            return location;
        }
    }

    public static class Serializer extends StdSerializer<Location> {

        protected Serializer(Class<Location> t) {
            super(t);
        }

        @Override
        public void serialize(Location value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("id", value.getId());
            gen.writeStringField("name", value.getName());
            gen.writeStringField("city", value.getCity());
            gen.writeStringField("country", value.getCountry());

            gen.writeStringField("center", value.getCenter());
            // TODO points: {type: "polygon", "coordinates": [[lng, lat]]}

            gen.writeNumberField("updatedDate", value.getUpdatedDate().getTime());
            gen.writeEndObject();
        }
    }
}
