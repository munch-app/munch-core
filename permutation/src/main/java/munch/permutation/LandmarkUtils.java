package munch.permutation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import munch.data.location.Area;
import munch.data.location.Landmark;
import munch.data.location.Location;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 31/10/18
 * Time: 1:57 AM
 * Project: munch-core
 */
public final class LandmarkUtils {

    public static Area asArea(Landmark landmark, double radiusKm) {
        Area area = new Area();
        area.setName(landmark.getName());
        area.setType(Area.Type.Generated);
        area.setLocation(new Location());

        Location.Polygon polygon = new Location.Polygon();
        polygon.setPoints(createPolygonPoints(landmark.getLocation().getLatLng(), radiusKm));
        area.getLocation().setPolygon(polygon);
        return area;
    }

    /**
     * @param latLng center
     * @param radius in km
     * @return List of Points in Polygon
     */
    public static List<String> createPolygonPoints(String latLng, double radius) {
        return toPoints(createPolygon(latLng, radius));
    }

    /**
     * @param latLng center
     * @param radius in km
     * @return Polygon
     */
    public static Polygon createPolygon(String latLng, double radius) {
        double distance = (1 / 110.54) * radius;

        GeometryFactory geometryFactory = new GeometryFactory();

        String[] split = latLng.split(",");

        Point point = geometryFactory.createPoint(
                new Coordinate(
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[0])
                )
        );
        return (Polygon) point.buffer(distance);
    }

    public static List<String> toPoints(Polygon polygon) {
        return Arrays.stream(polygon.getCoordinates())
                .map(c -> c.y + "," + c.x)
                .collect(Collectors.toList());
    }
}
