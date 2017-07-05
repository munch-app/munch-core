package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.search.data.Location;
import munch.search.data.SearchQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public class BoolQuery {
    private final ObjectMapper mapper;

    @Inject
    public BoolQuery(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @param query SearchQuery for place
     * @return created bool node
     */
    public JsonNode make(SearchQuery query) {
        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", must(query.getQuery()));
        bool.set("must_not", mustNot(query.getFilter()));
        bool.set("filter", filter(query.getLocation(), query.getFilter()));
        return bool;
    }

    /**
     * Search with text on name
     *
     * @param query query string
     * @return JsonNode must filter
     */
    private JsonNode must(String query) {
        ObjectNode must = mapper.createObjectNode();

        // Match all if query is blank
        if (StringUtils.isBlank(query)) return must.set("match_all", mapper.createObjectNode());

        // Match name if got query
        ObjectNode match = mapper.createObjectNode();
        match.put("name", query);
        return must.set("match", match);
    }

    /**
     * Filter to must not
     *
     * @param filter filters
     * @return JsonNode must_not filter
     */
    private JsonNode mustNot(SearchQuery.Filter filter) {
        ArrayNode notArray = mapper.createArrayNode();
        if (filter.getTag() == null || filter.getTag().getNegatives() == null) return notArray;

        // Must not filters
        for (String tag : filter.getTag().getNegatives()) {
            notArray.add(filterTerm("tags", tag.toLowerCase()));
        }
        return notArray;
    }

    /**
     * @param location polygon geo query
     * @param filter   filters object
     * @return JsonNode bool filter
     */
    private JsonNode filter(Location location, SearchQuery.Filter filter) {
        ArrayNode filterArray = mapper.createArrayNode();

        // Polygon if location exists
        if (location != null && location.getPoints() != null) {
            filterArray.add(filterPolygon(location.getPoints()));
        }

        // Filter distance
        if (filter.getDistance() != null && filter.getDistance().getLatLng() != null) {
            if (filter.getDistance().getMin() != null || filter.getDistance().getMax() != null) {
                filterArray.add(filterDistanceRange(
                        filter.getDistance().getLatLng(), filter.getDistance().getMin(), filter.getDistance().getMax()));
            }
        }

        // Filter to positive tags
        if (filter.getTag() != null && filter.getTag().getNegatives() != null) {
            for (String tag : filter.getTag().getPositives()) {
                filterArray.add(filterTerm("tags", tag.toLowerCase()));
            }
        }

        // Filter price
        if (filter.getPrice() != null) {
            ObjectNode range = mapper.createObjectNode();
            if (filter.getPrice().getMax() != null) {
                range.put("lte", filter.getPrice().getMax());
            }

            if (filter.getPrice().getMin() != null) {
                range.put("gte", filter.getPrice().getMin());
            }

            // Only add if contains max or min
            if (range.size() != 0) {
                filterArray.add(mapper.createObjectNode().set("term", range));
            }
        }

        // TODO for Future: logic for ratings and hours
        return filterArray;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-distance-query.html
     *
     * @param latLng   center
     * @param distance distance query
     * @return JsonNode = { "geo_distance": { "distance": "1km", "location.latLng": "-12,23"}}
     */
    private JsonNode filterDistance(String latLng, String distance) {
        ObjectNode geoDistance = mapper.createObjectNode();
        geoDistance.put("distance", distance);
        geoDistance.put("location.latLng", latLng);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("geo_distance", geoDistance);
        return filter;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/5.4/query-dsl-geo-distance-range-query.html
     *
     * @param latLng center
     * @param min    min distance
     * @param max    max distance
     * @return JsonNode = { "geo_distance_range": { "from": "1km", "to": "2km", "location.latLng": "-12,23"}}
     */
    private JsonNode filterDistanceRange(String latLng, Integer min, Integer max) {
        ObjectNode geoDistance = mapper.createObjectNode();
        if (min != null)
            geoDistance.put("from", min + "m");
        if (max != null)
            geoDistance.put("to", max + "m");
        geoDistance.put("location.latLng", latLng);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("geo_distance_range", geoDistance);
        return filter;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-polygon-query.html
     *
     * @param pointList list of points to form a polygon
     * @return JsonNode = { "geo_polygon": { "location.latLng": { "points": ["-1,2", "-5,33" ...]}}}
     */
    private JsonNode filterPolygon(List<String> pointList) {
        ArrayNode points = mapper.createArrayNode();
        pointList.forEach(points::add);

        ObjectNode latLng = mapper.createObjectNode();
        latLng.set("points", points);

        ObjectNode geoPolygon = mapper.createObjectNode();
        geoPolygon.set("location.latLng", latLng);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("geo_polygon", geoPolygon);
        return filter;
    }

    /**
     * @param name name of term
     * @param text text of term
     * @return JsonNode =  { "term" : { "name" : "text" } }
     */
    private JsonNode filterTerm(String name, String text) {
        ObjectNode term = mapper.createObjectNode();
        term.put(name, text);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("term", term);
        return filter;
    }
}
