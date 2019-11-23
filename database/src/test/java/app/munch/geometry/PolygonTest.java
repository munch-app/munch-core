package app.munch.geometry;

import dev.fuxing.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 09:14
 */
class PolygonTest {

    static final String json = "{\"type\":\"polygon\",\"coordinates\":[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]]}";

    @Test
    void toJson() {
        Polygon polygon = new Polygon();
        polygon.setCoordinates(List.of(List.of(
                new Coordinate(100.0, 0.0),
                new Coordinate(101.0, 0.0),
                new Coordinate(101.0, 1.0),
                new Coordinate(100.0, 1.0),
                new Coordinate(100.0, 0.0)
        )));

        assertEquals(GeometryType.POLYGON, polygon.getType());
        assertEquals(json, JsonUtils.toString(polygon));
    }

    @Test
    void fromJson() {
        Geometry geometry = JsonUtils.toObject(json, Geometry.class);

        assertEquals(geometry.getClass(), Polygon.class);

        Polygon polygon = (Polygon) geometry;
        assertEquals(GeometryType.POLYGON, polygon.getType());
        assertEquals(1, polygon.getCoordinates().size());

        List<Coordinate> list = polygon.getCoordinates().get(0);
        assertEquals(100.0, list.get(0).getLongitude());
        assertEquals(0.0, list.get(0).getLatitude());

        assertEquals(101.0, list.get(1).getLongitude());
        assertEquals(0.0, list.get(1).getLatitude());

        assertEquals(101.0, list.get(2).getLongitude());
        assertEquals(1.0, list.get(2).getLatitude());

        assertEquals(100.0, list.get(3).getLongitude());
        assertEquals(1.0, list.get(3).getLatitude());

        assertEquals(100.0, list.get(4).getLongitude());
        assertEquals(0.0, list.get(4).getLatitude());
    }
}
