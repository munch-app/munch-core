package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by: Fuxing
 * Date: 8/7/2017
 * Time: 4:55 PM
 * Project: munch-core
 */
@Singleton
public class LocationBoolQuery {
    private final ObjectMapper mapper;

    @Inject
    public LocationBoolQuery(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @param lat latitude
     * @param lng longitude
     * @return bool reverse query
     */
    public JsonNode reverse(double lat, double lng) {
        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", must(null));
        bool.set("filter", filter(lat, lng));
        return null;
    }

    /**
     * @param text text query
     * @return bool search query
     */
    public JsonNode search(String text) {
        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", must(text));
        return bool;
    }

    /**
     * @param query query, can be null
     * @return { "match_all": {} } OR { "match": { "name": query }}
     */
    private JsonNode must(String query) {
        ObjectNode must = mapper.createObjectNode();

        // Match all if query is blank
        if (StringUtils.isBlank(query)) {
            return must.set("match_all", mapper.createObjectNode());
        }

        // Match name if got query
        ObjectNode match = mapper.createObjectNode();
        match.put("name", query);
        return must.set("match", match);
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-shape-query.html
     *
     * @param lat latitude
     * @param lng longitude
     * @return { "geo_shape": { "points": { "shape": { "type": "point", "coordinates": [lat, lng] }}}}
     */
    private JsonNode filter(double lat, double lng) {
        ObjectNode shape = mapper.createObjectNode();
        shape.put("type", "point");
        shape.set("coordinates", mapper.createArrayNode().add(lng).add(lat));

        ObjectNode points = mapper.createObjectNode();
        points.set("shape", shape);

        ObjectNode geoShape = mapper.createObjectNode();
        geoShape.set("points", points);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("geo_shape", geoShape);
        return filter;
    }
}
