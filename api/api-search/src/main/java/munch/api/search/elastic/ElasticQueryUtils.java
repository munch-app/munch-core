package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Singleton;
import edit.utils.LatLngUtils;
import munch.api.search.SearchQuery;
import munch.api.search.SearchRequest;
import munch.data.elastic.ElasticUtils;
import munch.data.location.Area;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
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
     * @param filters additional filters
     * @return BoolQuery for Place data
     */
    public static JsonNode make(SearchRequest request, JsonNode... filters) {
        ArrayNode filterArray = filter(request);
        for (JsonNode filter : filters) {
            filterArray.add(filter);
        }
        return make(filterArray);
    }

    /**
     * NOTE: This ElasticQueryUtils is only for Place data type
     *
     * @param node filter or filters
     * @return created Bool Node
     */
    public static JsonNode make(JsonNode node) {
        ObjectNode bool = mapper.createObjectNode();

        bool.set("must", ElasticUtils.mustMatchAll());
        bool.set("filter", node);
        return mapper.createObjectNode().set("bool", bool);
    }

    /**
     * @param request SearchRequest.SearchQuery.Filter
     * @return JsonNode bool filter
     */
    private static ArrayNode filter(SearchRequest request) {
        SearchQuery searchQuery = request.getSearchQuery();

        ArrayNode filterArray = mapper.createArrayNode();
        filterArray.add(ElasticUtils.filterTerm("dataType", "Place"));
        filterArray.add(ElasticUtils.filterTerm("status.type", "open"));

        // Filter 'Container' else 'Location' else 'LatLng' else none
        filterLocation(request).ifPresent(filterArray::add);

        // Check if filter is not null before continuing
        SearchQuery.Filter filter = searchQuery.getFilter();
        if (filter == null) return filterArray;

        // Filter to positive tags
        filter.getTags().forEach(tag -> {
            if (tag.getTagId() == null) return;
            filterArray.add(ElasticUtils.filterTerm("tags.tagId", tag.getTagId()));
        });

        // Filter price
        filterPrice(filter.getPrice()).ifPresent(filterArray::add);

        // Filter hour
        filterHour(filter.getHour()).ifPresent(filterArray::add);

        if (request.isBetween()) {
            filterArray.add(ElasticUtils.filterRange("taste.group", "gte", 2));
        }

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
                .putObject("hour." + StringUtils.lowerCase(hour.getDay()) + ".open_close")
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
            return ElasticUtils.filterTerm("areas.areaId", area.getAreaId());
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
