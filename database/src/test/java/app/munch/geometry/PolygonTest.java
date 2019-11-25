package app.munch.geometry;

import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void invalidLT4() {
        Polygon polygon = new Polygon();
        polygon.setCoordinates(List.of(List.of(
                new Coordinate(100.0, 0.0),
                new Coordinate(101.0, 0.0),
                new Coordinate(101.0, 1.0)
        )));

        assertThrows(ValidationException.class, () -> {
            ValidationException.validate(polygon);
        });
    }

    @Test
    void invalidFirstLast() {
        Polygon polygon = new Polygon();
        polygon.setCoordinates(List.of(List.of(
                new Coordinate(100.0, 0.0),
                new Coordinate(101.0, 0.0),
                new Coordinate(101.0, 1.0),
                new Coordinate(100.0, 1.0)
        )));

        assertThrows(ValidationException.class, () -> {
            ValidationException.validate(polygon);
        });
    }

    @Test
    void validClosure() {
        Polygon polygon = new Polygon();
        polygon.setCoordinates(List.of(List.of(
                new Coordinate(100.0, 0.0),
                new Coordinate(101.0, 0.0),
                new Coordinate(101.0, 1.0),
                new Coordinate(100.0, 1.0),
                new Coordinate(100.0, 0.0)
        )));

        ValidationException.validate(polygon);
    }

    /**
     * Valid Singapore Polygon Area
     */
    @Test
    void validSingapore() {
        Polygon polygon = new Polygon();
        polygon.setCoordinates(List.of(List.of(
                new Coordinate(103.94733843085235, 1.345639521023323),
                new Coordinate(103.94728628306984, 1.3451100557021285),
                new Coordinate(103.94713184373235, 1.3446009374363),
                new Coordinate(103.94688104785698, 1.3441317313552763),
                new Coordinate(103.9465435333886, 1.3437204687856175),
                new Coordinate(103.94613227081895, 1.3433829543172369),
                new Coordinate(103.94566306473791, 1.3431321584418738),
                new Coordinate(103.94515394647209, 1.3429777191043708),
                new Coordinate(103.9446244811509, 1.3429255713218575),
                new Coordinate(103.9440950158297, 1.3429777191043708),
                new Coordinate(103.94358589756388, 1.3431321584418738),
                new Coordinate(103.94311669148284, 1.3433829543172369),
                new Coordinate(103.94270542891319, 1.3437204687856175),
                new Coordinate(103.94236791444482, 1.3441317313552763),
                new Coordinate(103.94211711856944, 1.3446009374363),
                new Coordinate(103.94196267923195, 1.3451100557021285),
                new Coordinate(103.94191053144944, 1.345639521023323),
                new Coordinate(103.94196267923195, 1.3461689863445174),
                new Coordinate(103.94211711856944, 1.346678104610346),
                new Coordinate(103.94236791444482, 1.3471473106913696),
                new Coordinate(103.94270542891319, 1.3475585732610285),
                new Coordinate(103.94311669148284, 1.347896087729409),
                new Coordinate(103.94358589756388, 1.348146883604772),
                new Coordinate(103.9440950158297, 1.3483013229422751),
                new Coordinate(103.9446244811509, 1.3483534707247884),
                new Coordinate(103.94515394647209, 1.3483013229422751),
                new Coordinate(103.94566306473791, 1.348146883604772),
                new Coordinate(103.94613227081895, 1.347896087729409),
                new Coordinate(103.9465435333886, 1.3475585732610285),
                new Coordinate(103.94688104785698, 1.3471473106913696),
                new Coordinate(103.94713184373235, 1.346678104610346),
                new Coordinate(103.94728628306984, 1.3461689863445174),
                new Coordinate(103.94733843085235, 1.345639521023323)
        )));

        ValidationException.validate(polygon);
    }
}
