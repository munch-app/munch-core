package munch.api.search.elastic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import edit.utils.LatLngUtils;
import munch.api.search.SearchQuery;
import munch.restful.core.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 17/10/18
 * Time: 1:01 AM
 * Project: munch-core
 */
public final class ElasticSpatialUtils {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private final static String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    private final static String[] GROUP_4 = new String[]{"0123", "4567", "89bc", "defg", "hjkm", "npqr", "stuv", "wxyz"};
    private final static Map<String, Integer> GROUP_4_INT = new HashMap<>();

    static {
        for (int i = 0; i < GROUP_4.length; i++) {
            String letter = GROUP_4[i];

            for (String s : letter.split("")) {
                GROUP_4_INT.put(s, i);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(GROUP_4_INT);
    }

    public static JsonNode filterBoundingBox(String latLng, double latOffsetMeter, double lngOffsetMeter) {
        LatLngUtils.LatLng parsed = LatLngUtils.parse(latLng);
        return filterBoundingBox(parsed.getLat(), parsed.getLng(), latOffsetMeter, lngOffsetMeter);
    }

    /**
     * @return maximum latitude differences in points
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static double getMaxLatDiff(List<SearchQuery.Filter.Location.Point> points) {
        List<LatLngUtils.LatLng> latLngs = points.stream().map(point -> LatLngUtils.parse(point.getLatLng())).collect(Collectors.toList());
        double min = latLngs.stream().mapToDouble(LatLngUtils.LatLng::getLat).min().getAsDouble();
        double max = latLngs.stream().mapToDouble(LatLngUtils.LatLng::getLat).max().getAsDouble();
        return max - min;
    }

    /**
     * @return maximum longitude differences in points
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static double getMaxLngDiff(List<SearchQuery.Filter.Location.Point> points) {
        List<LatLngUtils.LatLng> latLngs = points.stream().map(point -> LatLngUtils.parse(point.getLatLng())).collect(Collectors.toList());
        double min = latLngs.stream().mapToDouble(LatLngUtils.LatLng::getLng).min().getAsDouble();
        double max = latLngs.stream().mapToDouble(LatLngUtils.LatLng::getLng).max().getAsDouble();
        return max - min;
    }

    public static JsonNode filterBoundingBox(double lat, double lng, double latOffsetMeter, double lngOffsetMeter) {
        String[] boundingBox = ElasticSpatialUtils.getBoundingBox(lat, lng, latOffsetMeter / 1000D, lngOffsetMeter / 1000D);

        ObjectNode filter = JsonUtils.createObjectNode();
        filter.putObject("geo_bounding_box")
                .putObject("location.latLng")
                .put("top_left", boundingBox[0])
                .put("bottom_right", boundingBox[1]);
        return filter;
    }

    public static String groupInto4(String hash) {
        String substring = hash.substring(0, hash.length() - 1);
        return substring + getGroup4(hash);
    }

    public static int getGroup4(String hash) {
        int index = hash.length() - 1;
        String h = hash.substring(index, index + 1).toLowerCase();
        return GROUP_4_INT.get(h);
    }

    public static boolean isGroup4(String hash, String compare) {
        if (hash.length() != compare.length())
            throw new IllegalStateException("length of hash and compare must be the same");

        return getGroup4(hash) == getGroup4(compare);
    }

    public static Polygon getPolygon(List<String> points) {
        return geometryFactory.createPolygon(points.stream()
                .map(s -> {
                    String[] split = s.split(",");
                    return new Coordinate(
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[0])
                    );
                }).toArray(Coordinate[]::new));
    }


    public static String[] getBoundingBox(double lat, double lng, double latOffsetKm, double lngOffsetKm) {
        final double latOffset = ElasticSpatialUtils.toRad(latOffsetKm);
        final double lngOffset = ElasticSpatialUtils.toRad(lngOffsetKm);
        return new String[]{
                (lat + latOffset) + "," + (lng - lngOffset), // Top Lat, Lng
                (lat - latOffset) + "," + (lng + lngOffset), // Bot Lat, Lng
        };
    }

    public static <T> double[] getCentroid(List<T> list, Function<T, String> mapper) {
        List<String> points = list.stream()
                .map(mapper)
                .collect(Collectors.toList());
        return getCentroid(points);
    }

    /**
     * @return centroid of points
     */
    public static double[] getCentroid(List<String> points) {
        double centroidLat = 0, centroidLng = 0;

        for (String point : points) {
            LatLngUtils.LatLng latLng = LatLngUtils.parse(point);
            centroidLat += latLng.getLat();
            centroidLng += latLng.getLng();
        }

        return new double[]{
                centroidLat / points.size(),
                centroidLng / points.size()
        };
    }

    public static double toRad(double radiusInKm) {
        return (1 / 110.54) * radiusInKm;
    }
}
