package app.munch.geometry;

import dev.fuxing.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 09:20
 */
class MultiPolygonTest {
    static final String json = "{" +
            "\"type\":\"multipolygon\"," +
            "\"coordinates\":[" +
            "[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]]," +
            "[[[100.0,0.0],[101.0,0.0],[101.0,1.0],[100.0,1.0],[100.0,0.0]]," +
            "[[100.2,0.2],[100.8,0.2],[100.8,0.8],[100.2,0.8],[100.2,0.2]]]" +
            "]" +
            "}";

    @Test
    void toJson() {
        MultiPolygon multiPolygon = new MultiPolygon();
        multiPolygon.setCoordinates(List.of(
                List.of(
                        List.of(
                                new Coordinate(102.0, 2.0),
                                new Coordinate(103.0, 2.0),
                                new Coordinate(103.0, 3.0),
                                new Coordinate(102.0, 3.0),
                                new Coordinate(102.0, 2.0)
                        )
                ),
                List.of(
                        List.of(
                                new Coordinate(100.0, 0.0),
                                new Coordinate(101.0, 0.0),
                                new Coordinate(101.0, 1.0),
                                new Coordinate(100.0, 1.0),
                                new Coordinate(100.0, 0.0)
                        ),
                        List.of(
                                new Coordinate(100.2, 0.2),
                                new Coordinate(100.8, 0.2),
                                new Coordinate(100.8, 0.8),
                                new Coordinate(100.2, 0.8),
                                new Coordinate(100.2, 0.2)
                        )
                )
        ));

        assertEquals(GeometryType.MULTIPOLYGON, multiPolygon.getType());
        assertEquals(json, JsonUtils.toString(multiPolygon));
    }

    @Test
    void fromJson() {
        Geometry geometry = JsonUtils.toObject(json, Geometry.class);

        assertEquals(geometry.getClass(), MultiPolygon.class);
        MultiPolygon multiPolygon = (MultiPolygon) geometry;
        assertEquals(GeometryType.MULTIPOLYGON, multiPolygon.getType());
        assertEquals(2, multiPolygon.getCoordinates().size());


        List<List<Coordinate>> first = multiPolygon.getCoordinates().get(0);
        assertEquals(1, first.size());
        List<Coordinate> list = first.get(0);

        assertEquals(102.0, list.get(0).getLongitude());
        assertEquals(2.0, list.get(0).getLatitude());

        assertEquals(103.0, list.get(1).getLongitude());
        assertEquals(2.0, list.get(1).getLatitude());

        assertEquals(103.0, list.get(2).getLongitude());
        assertEquals(3.0, list.get(2).getLatitude());

        assertEquals(102.0, list.get(3).getLongitude());
        assertEquals(3.0, list.get(3).getLatitude());

        assertEquals(102.0, list.get(4).getLongitude());
        assertEquals(2.0, list.get(4).getLatitude());


        List<List<Coordinate>> second = multiPolygon.getCoordinates().get(1);
        assertEquals(2, second.size());

        list = second.get(0);
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

        list = second.get(1);
        assertEquals(100.2, list.get(0).getLongitude());
        assertEquals(0.2, list.get(0).getLatitude());

        assertEquals(100.8, list.get(1).getLongitude());
        assertEquals(0.2, list.get(1).getLatitude());

        assertEquals(100.8, list.get(2).getLongitude());
        assertEquals(0.8, list.get(2).getLatitude());

        assertEquals(100.2, list.get(3).getLongitude());
        assertEquals(0.8, list.get(3).getLatitude());

        assertEquals(100.2, list.get(4).getLongitude());
        assertEquals(0.2, list.get(4).getLatitude());
    }
}
