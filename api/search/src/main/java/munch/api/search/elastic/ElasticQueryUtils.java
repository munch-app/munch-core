package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.data.location.Area;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
        ObjectNode bool = mapper.createObjectNode();
        bool.set("must", mustMatchAll());
        bool.set("must_not", mustNot(request));
        bool.set("filter", filter(request));
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
    private static JsonNode filter(SearchRequest request) {
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
        if (filter.getTag() != null && filter.getTag().getPositives() != null) {
            for (String tag : filter.getTag().getPositives()) {
                filterArray.add(filterTerm("tags.name", tag.toLowerCase()));
            }
        }

        // Filter price
        filterPrice(filter.getPrice()).ifPresent(filterArray::add);

        // Filter hour
        filterHour(filter.getHour()).ifPresent(filterArray::add);
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
                .putObject("hour." + hour.getDay() + ".open_close")
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
        SearchQuery searchQuery = request.getSearchQuery();
        SearchQuery.Filter filter = searchQuery.getFilter();

        JsonNode areaFilter = filterArea(filter.getArea());
        if (areaFilter != null) return Optional.of(areaFilter);

        if (request.getLatLng() != null) {
            JsonNode filtered = filterDistance(request.getLatLng(), request.getRadius());
            return Optional.of(filtered);
        }

        return Optional.empty();
    }

    @Nullable
    public static JsonNode filterArea(Area area) {
        if (area == null) return null;
        if (area.getAreaId() == null) return null;
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
