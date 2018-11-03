package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import edit.utils.LatLngUtils;
import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.data.location.Area;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:22 PM
 * Project: munch-core
 */
@Singleton
public final class ElasticQueryUtils {
    private static final Logger logger = LoggerFactory.getLogger(ElasticQueryUtils.class);
    private static final ObjectMapper mapper = JsonUtils.objectMapper;

    private static final Pattern TIME_PATTERN = Pattern.compile(":");

    /**
     * NOTE: This ElasticQueryUtils is only for Place data type
     *
     * @param request SearchRequest
     * @return created bool node
     */
    public static JsonNode make(SearchRequest request) {
        return make(mustNot(request), filter(request));
    }

    /**
     * @param filters to fill
     * @return created bool node
     */
    public static JsonNode make(ArrayNode filters) {
        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", mustMatchAll());
        bool.set("filter", filters);
        return mapper.createObjectNode().set("bool", bool);
    }

    /**
     * NOTE: This ElasticQueryUtils is only for Place data type
     *
     * @param request SearchRequest
     * @param filters additonal filters
     * @return created bool node
     */
    public static JsonNode make(SearchRequest request, JsonNode... filters) {
        ArrayNode filterArray = filter(request);
        for (JsonNode filter : filters) {
            filterArray.add(filter);
        }
        return make(mustNot(request), filterArray);
    }

    /**
     * NOTE: This ElasticQueryUtils is only for Place data type
     */
    public static JsonNode make(JsonNode mustNot, JsonNode filter) {
        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", mustMatchAll());
        bool.set("must_not", mustNot);
        bool.set("filter", filter);
        return mapper.createObjectNode().set("bool", bool);
    }

    /**
     * Search with text on name
     *
     * @return JsonNode must filter
     */
    public static JsonNode mustMatchAll() {
        ObjectNode root = mapper.createObjectNode();
        root.putObject("match_all");
        return root;
    }

    public static JsonNode multiMatch(String query, String field, String... fields) {
        ObjectNode root = JsonUtils.createObjectNode();
        ObjectNode match = root.putObject("multi_match");

        match.put("query", query);
        match.put("type", "phrase_prefix");
        ArrayNode fieldsNode = match.putArray("fields");
        fieldsNode.add(field);
        for (String each : fields) {
            fieldsNode.add(each);
        }
        return root;
    }

    /**
     * Filter to must not
     *
     * @param request SearchRequest.SearchQuery.Filter
     * @return JsonNode must_not filter
     */
    private static JsonNode mustNot(SearchRequest request) {
        SearchQuery.Filter filter = request.getSearchQuery().getFilter();

        ArrayNode notArray = mapper.createArrayNode();
        if (filter == null) return notArray;
        if (filter.getTag() == null) return notArray;
        if (filter.getTag().getNegatives() == null) return notArray;

        // Must not filters
        for (String tag : filter.getTag().getNegatives()) {
            notArray.add(filterTerm("tags.name", tag.toLowerCase()));
        }
        return notArray;
    }

    /**
     * @param request SearchRequest.SearchQuery.Filter
     * @return JsonNode bool filter
     */
    private static ArrayNode filter(SearchRequest request) {
        SearchQuery searchQuery = request.getSearchQuery();

        ArrayNode filterArray = mapper.createArrayNode();
        filterArray.add(filterTerm("dataType", "Place"));
        filterArray.add(filterTerm("status.type", "open"));

        // Filter 'Container' else 'Location' else 'LatLng' else none
        filterLocation(request).ifPresent(filterArray::add);

        // Check if filter is not null before continuing
        SearchQuery.Filter filter = searchQuery.getFilter();
        if (filter == null) return filterArray;

        // Filter to positive tags
        Set<String> positives = new HashSet<>();

        if (filter.getTag() != null && filter.getTag().getPositives() != null) {
            for (String tag : filter.getTag().getPositives()) {
                positives.add(tag.toLowerCase());
            }
        }

        // Filter price
        filterPrice(filter.getPrice()).ifPresent(filterArray::add);

        // Filter hour
        filterHour(filter.getHour()).ifPresent(filterArray::add);
        if (filter.getHour() != null) {
            if (StringUtils.isNotBlank(filter.getHour().getName()) && filter.getHour().getOpen() == null) {
                positives.add(filter.getHour().getName().toLowerCase());
            }
        }

        if (request.isBetween()) {
            filterArray.add(filterRange("taste.group", "gte", 2));
        }

        // Accumulate positive tags
        positives.forEach(s -> {
            filterArray.add(filterTerm("tags.name", s));
        });
        return filterArray;
    }

    /**
     * @param price SearchQuery.Filter.Price filters
     * @return Filter Price Json
     */
    private static Optional<JsonNode> filterPrice(SearchQuery.Filter.Price price) {
        if (price == null) return Optional.empty();

        ObjectNode range = mapper.createObjectNode();
        if (price.getMax() != null) {
            range.put("lte", price.getMax());
        }

        if (price.getMin() != null) {
            range.put("gte", price.getMin());
        }

        // Only add if contains max or min
        if (range.size() > 0) {
            // Filter is applied on middle
            ObjectNode rangeFilter = mapper.createObjectNode();
            rangeFilter.putObject("range").set("price.perPax", range);
            return Optional.of(rangeFilter);
        }

        return Optional.empty();
    }

    /**
     * @param hour SearchQuery.Filter.Hour hour filters estimation
     * @return Filter Hour for Json
     */
    private static Optional<JsonNode> filterHour(SearchQuery.Filter.Hour hour) {
        if (hour == null) return Optional.empty();
        if (StringUtils.isAnyBlank(hour.getOpen(), hour.getClose(), hour.getDay())) return Optional.empty();

        ObjectNode rangeFilter = mapper.createObjectNode();
        // Open/Close time intersects
        rangeFilter.putObject("range")
                .putObject("hour." + hour.getDay().toLowerCase() + ".open_close")
                .put("relation", "intersects")
                .put("gte", ElasticQueryUtils.timeAsInt(hour.getOpen()))
                .put("lte", ElasticQueryUtils.timeAsInt(hour.getClose()));
        return Optional.of(rangeFilter);
    }

    /**
     * @param request SearchRequest
     * @return Filter Location Json
     */
    public static Optional<JsonNode> filterLocation(SearchRequest request) {
        if (request.isNearby()) {
            return Optional.of(filterDistance(request.getLatLng(), request.getRadius()));
        }

        if (request.isAnywhere()) {
            // Currently anywhere don't filter to any city, in the future it will auto find a city
            // via search request before reaching Elastic Query Utils
            return Optional.empty();
        }

        if (request.isWhere()) {
            if (request.getAreas().size() == 1) return Optional.ofNullable(filterArea(request.getAreas().get(0)));
            return Optional.ofNullable(filterAreas(request.getAreas()));
        }

        if (request.isBetween()) {
            // Can expand search as wide a possible as the results will be sorted in distance from centroid
            String centroid = request.getPointsCentroid();
            List<SearchQuery.Filter.Location.Point> points = request.getPoints();

            JsonNode filter;
            if (points.size() == 1) {
                filter = ElasticSpatialUtils.filterBoundingBox(centroid, 1500, 1500);
            } else {
                double latDiff = ElasticSpatialUtils.getMaxLatDiff(points);
                double lngDiff = ElasticSpatialUtils.getMaxLngDiff(points);
                if (latDiff < lngDiff) {
                    filter = ElasticSpatialUtils.filterBoundingBox(centroid, 5000, 1500);
                } else {
                    filter = ElasticSpatialUtils.filterBoundingBox(centroid, 1500, 5000);
                }
            }
            return Optional.of(filter);
        }

        return Optional.empty();
    }

    public static JsonNode filterAreas(List<Area> areas) {
        ArrayNode should = mapper.createArrayNode();
        for (Area area : areas) {
            JsonNode node = filterArea(area);
            if (node != null) should.add(filterArea(area));
        }


        ObjectNode boolNode = mapper.createObjectNode();
        boolNode.set("should", should);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("bool", boolNode);
        return rootNode;
    }

    @Nullable
    public static JsonNode filterArea(Area area) {
        if (area == null) return null;
        if (area.getLocation() == null) return null;
        if (area.getLocation().getPolygon() == null) return null;

        if (area.getType() == Area.Type.Cluster) {
            return filterTerm("areas.areaId", area.getAreaId());
        }

        List<String> points = area.getLocation().getPolygon().getPoints();
        if (points == null) return null;

        if (points.size() < 3) {
            logger.warn("SearchQuery.Filter.Location.points < 3. SearchQuery.Filter: {}", area.getLocation().getPolygon());
            return null;
        }

        return filterPolygon(points);
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-polygon-query.html
     *
     * @param pointList list of points to form a polygon
     * @return JsonNode = { "geo_polygon": { "location.latLng": { "points": ["-1,2", "-5,33" ...]}}}
     */
    public static JsonNode filterPolygon(List<String> pointList) {
        ObjectNode filter = mapper.createObjectNode();
        ArrayNode points = filter.putObject("geo_polygon")
                .putObject("location.latLng")
                .putArray("points");
        pointList.forEach(points::add);
        return filter;
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-geo-distance-query.html
     *
     * @param latLng latLng center
     * @param metres metres in distance
     * @return JsonNode = { "geo_distance": { "distance": "1km", "location.latLng": "-1,2"}}
     */
    public static JsonNode filterDistance(String latLng, double metres) {
        ObjectNode filter = mapper.createObjectNode();
        filter.putObject("geo_distance")
                .put("distance", metres + "m")
                .put("location.latLng", latLng);
        return filter;
    }

    public static JsonNode filterIntersects(String latLng) {
        ObjectNode filter = mapper.createObjectNode();
        ObjectNode polygon = filter.putObject("geo_shape")
                .putObject("location.polygon");

        LatLngUtils.LatLng point = LatLngUtils.parse(latLng);
        polygon.putObject("shape")
                .put("type", "point")
                .putArray("coordinates")
                .add(point.getLng())
                .add(point.getLat());
        polygon.put("relation", "intersects");
        return filter;
    }

    /**
     * @param name name of term
     * @param text text of term
     * @return JsonNode =  { "term" : { "name" : "text" } }
     */
    public static JsonNode filterTerm(String name, String text) {
        ObjectNode filter = mapper.createObjectNode();
        filter.putObject("term").put(name, text);
        return filter;
    }

    /**
     * @param name name of term
     * @param num  value to filter
     * @return JsonNode =  { "term" : { "name" : "text" } }
     */
    public static JsonNode filterTerm(String name, int num) {
        ObjectNode filter = mapper.createObjectNode();
        filter.putObject("term").put(name, num);
        return filter;
    }

    public static JsonNode filterTerm(String name, boolean value) {
        ObjectNode filter = mapper.createObjectNode();
        filter.putObject("term").put(name, value);
        return filter;
    }

    /**
     * @param name  name of terms
     * @param texts texts of terms
     * @return JsonNode =  { "terms" : { "name" : "text" } }
     */
    public static JsonNode filterTerms(String name, Collection<String> texts) {
        ObjectNode filter = mapper.createObjectNode();
        ArrayNode terms = filter.putObject("terms").putArray(name);
        for (String text : texts) {
            terms.add(text.toLowerCase());
        }
        return filter;
    }

    /**
     * E.g. createdDate > 1000 is "createdDate", "gt", 1000
     *
     * @param name     name of field to filter
     * @param operator operator in english form, e.g. gte, lt
     * @param value    value to compare again
     * @return filter range json
     */
    public static JsonNode filterRange(String name, String operator, long value) {
        ObjectNode filter = mapper.createObjectNode();
        filter.putObject("range")
                .putObject(name)
                .put(operator, value);
        return filter;
    }

    /**
     * E.g. createdDate > 1000 is "createdDate", "gt", 1000
     *
     * @param name     name of field to filter
     * @param operator operator in english form, e.g. gte, lt
     * @param value    value to compare again
     * @return filter range json
     */
    public static JsonNode filterRange(String name, String operator, double value) {
        ObjectNode filter = mapper.createObjectNode();
        filter.putObject("range")
                .putObject(name)
                .put(operator, value);
        return filter;
    }

    public static int timeAsInt(String time) throws ParamException {
        try {
            String[] split = TIME_PATTERN.split(time);
            int hour = Integer.parseInt(split[0]);
            int min = Integer.parseInt(split[1]);
            return (hour * 60) + min;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            throw new ParamException("time");
        }
    }
}
