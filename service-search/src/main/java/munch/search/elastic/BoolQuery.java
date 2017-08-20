package munch.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.data.Location;
import munch.data.SearchQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public final class BoolQuery {
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
        ObjectNode root = mapper.createObjectNode();

        // Match all if query is blank
        if (StringUtils.isBlank(query)) {
            root.putObject("match_all");
            return root;
        }

        // Match name if got query
        root.putObject("match").put("name", query);
        return root;
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
        if (filter.getDistance() != null) {
            SearchQuery.Filter.Distance distance = filter.getDistance();
            if (distance.getMax() != null && distance.getLatLng() != null) {
                filterArray.add(filterDistance(distance.getLatLng(), distance.getMax()));
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

        // Future: logic for ratings and hours
        return filterArray;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-distance-query.html
     *
     * @param latLng center
     * @param max    distance query
     * @return JsonNode = { "geo_distance": { "distance": "1km", "location.latLng": "-12,23"}}
     */
    private JsonNode filterDistance(String latLng, Integer max) {
        ObjectNode geoDistance = mapper.createObjectNode()
                .put("distance", max + "m")
                .put("location.latLng", latLng);

        ObjectNode filter = mapper.createObjectNode();
        filter.set("geo_distance", geoDistance);
        return filter;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-polygon-query.html
     *
     * @param pointList list of points to form a polygon
     * @return JsonNode = { "geo_polygon": { "location.latLng": { "points": ["-1,2", "-5,33" ...]}}}
     */
    private JsonNode filterPolygon(List<String> pointList) {
        ObjectNode filter = mapper.createObjectNode();
        ArrayNode points = filter.putObject("geo_polygon")
                .putObject("location.latLng")
                .putArray("points");
        pointList.forEach(points::add);
        return filter;
    }

    /**
     * @param name name of term
     * @param text text of term
     * @return JsonNode =  { "term" : { "name" : "text" } }
     */
    private JsonNode filterTerm(String name, String text) {
        ObjectNode filter = mapper.createObjectNode();
        filter.putObject("term").put(name, text);
        return filter;
    }
}