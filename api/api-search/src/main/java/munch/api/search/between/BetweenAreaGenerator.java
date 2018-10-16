package munch.api.search.between;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Polygon;
import edit.utils.LatLngUtils;
import munch.api.search.SearchRequest;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.api.search.elastic.ElasticSpatialUtils;
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

    private final ElasticClient elasticClient;

    @Inject
    public BetweenAreaGenerator(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    public List<Area> generate(SearchCardInjector.Loader.Request request) {
        double[] centroid = ElasticSpatialUtils.getCentroid(request.getRequest().getPoints(), SearchQuery.Filter.Location.Point::getLatLng);
        LatLngUtils.LatLng latLng = new LatLngUtils.LatLng(centroid[0], centroid[1]);

        List<Area> areas = getGeoHashCount(request.getRequest()).entrySet().stream()
                .collect(Collectors.groupingBy(e -> ElasticSpatialUtils.groupInto4(e.getKey())))
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map((entries) -> {
                    Set<String> hashes = new HashSet<>();
                    int count = 0;
                    for (Map.Entry<String, Integer> entry : entries) {
                        count += entry.getValue();
                        hashes.add(entry.getKey());
                    }

                    return new HashGroup(hashes, count);
                })
                .filter(hashGroup -> hashGroup.count >= 20)
                .map(HashGroup::createArea)
                .sorted((o1, o2) -> {
                    LatLngUtils.LatLng l1 = LatLngUtils.parse(o1.getLocation().getLatLng());
                    LatLngUtils.LatLng l2 = LatLngUtils.parse(o2.getLocation().getLatLng());
                    Objects.requireNonNull(l1);
                    Objects.requireNonNull(l2);

                    return Double.compare(latLng.distance(l1), latLng.distance(l2));
                })
                .collect(Collectors.toList());

        if (areas.size() > 0) return areas;
        return fallBack(latLng.toString());
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
            Polygon polygon = ElasticSpatialUtils.getPolygon(area.getLocation().getPolygon().getPoints());
            Polygon buffered = (Polygon) polygon.buffer(ElasticSpatialUtils.toRad(0.15));
            List<String> points = Arrays.stream(buffered.getCoordinates())
                    .map(coordinate -> coordinate.y + "," + coordinate.x)
                    .collect(Collectors.toList());
            area.getLocation().getPolygon().setPoints(points);
        });
        return areas;
    }

    private Map<String, Integer> getGeoHashCount(SearchRequest request) {
        double[] centroid = ElasticSpatialUtils.getCentroid(request.getPoints(), SearchQuery.Filter.Location.Point::getLatLng);

        ObjectNode root = JsonUtils.createObjectNode();
        root.put("size", 0);
        root.set("query", ElasticQueryUtils.make(request,
                ElasticSpatialUtils.filterBoundingBox(centroid[0], centroid[1], 4000),
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

        Map<String, Integer> map = new HashMap<>();
        for (JsonNode bucket : buckets) {
            map.put(bucket.path("key").asText(), bucket.path("doc_count").asInt());
        }
        return map;
    }

    public final class HashGroup {
        private Set<String> hashes;
        private int count;

        public HashGroup(Set<String> hashes, int count) {
            this.hashes = hashes;
            this.count = count;
        }

        public Area createArea() {
            Location location = new Location();
            location.setLatLng(ElasticSpatialUtils.getCentroidFromHash(hashes));
            location.setPolygon(ElasticSpatialUtils.getConvexHull(hashes));

            Area area = new Area();
            area.setType(Area.Type.Generated);
            area.setLocation(location);
            return area;
        }
    }
}
