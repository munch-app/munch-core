package munch.api.search.between;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.algorithm.ConvexHull;
import com.vividsolutions.jts.geom.*;
import edit.utils.LatLngUtils;
import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.api.search.inject.SearchCardInjector;
import munch.data.client.ElasticClient;
import munch.data.location.Area;
import munch.data.location.Location;
import munch.restful.core.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 12/10/18
 * Time: 2:57 PM
 * Project: munch-core
 */
@Singleton
public final class BetweenAreaGenerator {
    private static final Logger logger = LoggerFactory.getLogger(BetweenAreaGenerator.class);
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private static final Comparator<Group> COMPARATOR;

    static {
        Comparator<Group> comparator = Comparator.comparingInt(value -> value.count);
        comparator = comparator.reversed();
        COMPARATOR = comparator;
    }

    private final ElasticClient elasticClient;

    @Inject
    public BetweenAreaGenerator(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    public List<Area> generate(SearchCardInjector.Loader.Request request) {
        List<Group> groups = group(request.getRequest());
        groups.sort(COMPARATOR);

        List<Group> collected = new ArrayList<>();
        groups.removeIf(group -> {
            if (group.count >= 30) {
                collected.add(group);
                return true;
            }
            return false;
        });

        // Remaining still in groups, join adjacent
        collected.addAll(joinAdjacent(groups));

        collected.sort(COMPARATOR);
        collected.removeIf(group -> group.count < 20);
        List<Group> reduced = collected.size() > 10 ? collected.subList(0, 10) : collected;
        List<Area> areas = reduced.stream()
                .map(this::createArea)
                .collect(Collectors.toList());

        double[] centroid = getCentroid(request.getRequest().getPoints());
        LatLngUtils.LatLng latLng = new LatLngUtils.LatLng(centroid[0], centroid[1]);

        areas.sort((o1, o2) -> {
            LatLngUtils.LatLng l1 = LatLngUtils.parse(o1.getLocation().getLatLng());
            LatLngUtils.LatLng l2 = LatLngUtils.parse(o2.getLocation().getLatLng());
            Objects.requireNonNull(l1);
            Objects.requireNonNull(l2);

            return Double.compare(latLng.distance(l1), latLng.distance(l2));
        });

        if (areas.size() > 1) return areas;
        return fallBack(centroid[0] + "," + centroid[1]);
    }

    private List<Area> fallBack(String centroid) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 20);

        ArrayNode filters = JsonUtils.createArrayNode();
        filters.add(ElasticQueryUtils.filterTerm("dataType", "Area"));
        filters.add(ElasticQueryUtils.filterTerms("type", List.of("Region", "Cluster")));
        filters.add(ElasticQueryUtils.filterRange("counts.total", "gte", 20));
        root.set("query", ElasticQueryUtils.make(filters));

        ArrayNode sortArray = JsonUtils.createArrayNode();
        sortArray.add(ElasticSortUtils.sortDistance(centroid));
        root.set("sort", sortArray);


        List<Area> areas = elasticClient.searchHitsHits(root);
        areas.forEach(area -> {
            area.setAreaId(null);
            area.setType(Area.Type.Generated);

            Objects.requireNonNull(area.getLocation().getPolygon());
            Polygon polygon = geometryFactory.createPolygon(area.getLocation().getPolygon().getPoints().stream()
                    .map(s -> {
                        String[] split = s.split(",");
                        return new Coordinate(
                                Double.parseDouble(split[1]),
                                Double.parseDouble(split[0])
                        );
                    }).toArray(Coordinate[]::new));
            Polygon buffered = (Polygon) polygon.buffer(toDistance(0.175));
            List<String> points = Arrays.stream(buffered.getCoordinates())
                    .map(coordinate -> coordinate.y + "," + coordinate.x)
                    .collect(Collectors.toList());
            area.getLocation().getPolygon().setPoints(points);
        });
        return areas;
    }

    private List<Group> joinAdjacent(List<Group> groups) {
        List<Set<Group>> joining = new ArrayList<>();
        for (Group base : groups) {
            Set<Group> joined = new HashSet<>();
            for (Group join : groups) {
                if (forbidden(joining, base, join)) continue;

                // Can be joined
                joined.add(base);
                joined.add(join);
            }

            if (joined.isEmpty()) continue;
            joining.add(joined);
        }

        return joining.stream()
                .map(joins -> joins.stream().reduce((left, right) -> {
                            left.hashes.addAll(right.hashes);
                            left.count += right.count;
                            return left;
                        }
                ).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean forbidden(List<Set<Group>> joining, Group base, Group join) {
        if (base == join) return true;
        for (Collection<Group> groups : joining) {
            for (Group group : groups) {
                if (group == join) return true;
            }
        }
        String baseHash = base.hashes.get(0);
        String joinHash = join.hashes.get(0);

        LatLngUtils.LatLng baseLatLng = LatLngUtils.parse(GeoHashUtils.decodeCenter(baseHash));
        LatLngUtils.LatLng joinLatLng = LatLngUtils.parse(GeoHashUtils.decodeCenter(joinHash));
        if (baseLatLng.distance(joinLatLng) > 200) return true;
        return false;
    }

    private Area createArea(Group group) {
        String centroid = getCentroid(group);

        Location location = new Location();
        location.setLatLng(centroid);
        location.setPolygon(createPolygon(group));

        Area area = new Area();
        area.setType(Area.Type.Generated);
        area.setLocation(location);
        return area;
    }

    private List<Group> group(SearchRequest request) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("size", 0);
        root.set("query", ElasticQueryUtils.make(request,
                getBoundingBoxFilter(request.getPoints()),
                ElasticQueryUtils.filterRange("taste.group", "gte", 1)
        ));

        root.putObject("aggregations")
                .putObject("cluster")
                .putObject("geohash_grid")
                .put("field", "location.latLng")
                .put("size", 3000)
                .put("precision", 7);

        JsonNode node = elasticClient.search(root);
        JsonNode buckets = node.path("aggregations")
                .path("cluster")
                .path("buckets");

        List<Group> groups = new ArrayList<>();
        for (JsonNode bucket : buckets) {
            groups.add(new Group(
                    bucket.path("key").asText(),
                    bucket.path("doc_count").asInt()
            ));
        }
        return groups;
    }

    private JsonNode getBoundingBoxFilter(List<SearchQuery.Filter.Location.Point> points) {
        String[] boundingBox = getBoundingBox(points);

        ObjectNode filter = JsonUtils.createObjectNode();
        filter.putObject("geo_bounding_box")
                .putObject("location.latLng")
                .put("top_left", boundingBox[0])
                .put("bottom_right", boundingBox[1]);
        return filter;
    }

    /**
     * @return [(topLat, topLng), (botLat, botLng)]
     */
    private String[] getBoundingBox(List<SearchQuery.Filter.Location.Point> points) {
        double[] centroid = getCentroid(points);

        double lat = centroid[0];
        double lng = centroid[1];

        final double offset = toDistance(1.8);
        return new String[]{
                (lat + offset) + "," + (lng - offset), // Top Lat, Lng
                (lat - offset) + "," + (lng + offset), // Bot Lat, Lng
        };
    }

    /**
     * @return centroid of points
     */
    private double[] getCentroid(List<SearchQuery.Filter.Location.Point> points) {
        double centroidLat = 0, centroidLng = 0;

        for (SearchQuery.Filter.Location.Point point : points) {
            LatLngUtils.LatLng latLng = LatLngUtils.parse(point.getLatLng());
            centroidLat += latLng.getLat();
            centroidLng += latLng.getLng();
        }

        return new double[]{
                centroidLat / points.size(),
                centroidLng / points.size()
        };
    }

    private Location.Polygon createPolygon(Group group) {
        double distance = toDistance(0.01);

        List<Coordinate> coordinates = new ArrayList<>();
        for (String hash : group.hashes) {
            Envelope envelope = GeoHashUtils.decodeEnvelope(hash);
            envelope.expandBy(distance); // 10 meters

            Polygon geometry = (Polygon) geometryFactory.toGeometry(envelope);
            coordinates.addAll(Arrays.asList(geometry.getCoordinates()));
        }

        ConvexHull convexHull = new ConvexHull(coordinates.toArray(new Coordinate[0]), geometryFactory);
        Polygon hull = (Polygon) convexHull.getConvexHull();
        List<String> points = Arrays.stream(hull.getCoordinates())
                .map(coordinate -> coordinate.y + "," + coordinate.x)
                .collect(Collectors.toList());

        Location.Polygon polygon = new Location.Polygon();
        polygon.setPoints(points);
        return polygon;
    }

    private String getCentroid(Group group) {
        if (group.hashes.size() == 1) {
            return GeoHashUtils.decodeCenter(group.hashes.get(0));
        }

        double centroidLat = 0, centroidLng = 0;

        for (String hash : group.hashes) {
            LatLngUtils.LatLng latLng = LatLngUtils.parse(GeoHashUtils.decodeCenter(hash));
            centroidLat += latLng.getLat();
            centroidLng += latLng.getLng();
        }

        return (centroidLat / group.hashes.size()) + "," + (centroidLng / group.hashes.size());
    }

    private double toDistance(double radiusInKm) {
        return (1 / 110.54) * radiusInKm;
    }

    public class Group {
        private final List<String> hashes = new ArrayList<>();
        private int count;

        public Group(String key, int count) {
            this.hashes.add(key);
            this.count = count;
        }
    }
}
