package munch.places.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.SearchQuery;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public class BoolQuery {
    private static final String DEFAULT_DISTANCE = "2km";

    // Create streamlined empty filter
    private static final SearchQuery.Filters emptyFilter = new SearchQuery.Filters();

    static {
        emptyFilter.setPriceRange(null);
        emptyFilter.setRatingsAbove(null);
        emptyFilter.setHours(Collections.emptySet());
        emptyFilter.setTags(Collections.emptySet());
    }

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
        // Fix filter to be more streamlined
        SearchQuery.Filters filters = fixFilters(query.getFilters());

        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", must(query.getQuery()));
        bool.set("must_not", mustNot(filters));
        bool.set("filter", filter(query.getLocation(), filters));
        return bool;
    }

    /**
     * Search with text on name
     *
     * @param query query string
     * @return JsonNode must filter
     */
    private JsonNode must(@Nullable String query) {
        ObjectNode must = mapper.createObjectNode();

        // Match all if no query
        if (query == null) return must.set("match_all", mapper.createObjectNode());

        // Match name if got query
        ObjectNode match = mapper.createObjectNode();
        match.put("name", query);
        return must.set("match", match);
    }

    /**
     * Filter to must not
     *
     * @param filters filters list
     * @return JsonNode must_not filter
     */
    private JsonNode mustNot(SearchQuery.Filters filters) {
        ArrayNode notArray = mapper.createArrayNode();

        // Must not filters
        for (SearchQuery.Filters.Tag tag : filters.getTags()) {
            if (!tag.isPositive()) {
                notArray.add(filterTerm("tag", tag.getText()));
            }
        }
        return notArray;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-distance-query.html
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-polygon-query.html
     *
     * @param location polygon geo query
     * @param filters  filters object
     * @return JsonNode bool filter
     */
    private JsonNode filter(@Nullable SearchQuery.Location location, SearchQuery.Filters filters) {
        ArrayNode filterArray = mapper.createArrayNode();

        // Distance geo query first else then polygon
        if (location != null) {
            if (location.getPoints() == null || location.getPoints().isEmpty() && StringUtils.isNotBlank(location.getCenter())) {
                filterArray.add(filterDistance(location.getCenter(), DEFAULT_DISTANCE));
            } else {
                filterArray.add(filterPolygon(location.getPoints()));
            }
        }

        // Filter to positive tags
        for (SearchQuery.Filters.Tag tag : filters.getTags()) {
            if (tag.isPositive()) {
                filterArray.add(filterTerm("tag", tag.getText()));
            }
        }

        // Future TODO logic for price, ratings and hours
        return filterArray;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-distance-query.html
     *
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

    /**
     * Fix filter to be more streamlined concise code for above
     *
     * @param filters filters to fix
     * @return filters
     */
    private static SearchQuery.Filters fixFilters(SearchQuery.Filters filters) {
        if (filters == null) filters = emptyFilter;
        if (filters.getHours() == null) filters.setHours(Collections.emptySet());
        if (filters.getTags() == null) filters.setTags(Collections.emptySet());
        return filters;
    }
}
