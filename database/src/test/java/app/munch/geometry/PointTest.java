package app.munch.geometry;

import dev.fuxing.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 09:06
 */
class PointTest {
    static final String json = "{\"type\":\"point\",\"coordinates\":[-77.03653,38.897676]}";

    @Test
    void toJson() {
        Point point = new Point();
        point.setCoordinates(new Coordinate(-77.03653, 38.897676));

        assertEquals(GeometryType.POINT, point.getType());
        assertEquals(json, JsonUtils.toString(point));
    }

    @Test
    void fromJson() {
        Geometry geometry = JsonUtils.toObject(json, Geometry.class);

        assertEquals(geometry.getClass(), Point.class);

        Point point = (Point) geometry;
        assertEquals(GeometryType.POINT, point.getType());
        assertEquals(38.897676, point.getCoordinates().getLatitude());
        assertEquals(-77.03653, point.getCoordinates().getLongitude());
    }
}
