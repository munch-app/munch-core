package app.munch.geometry;

import dev.fuxing.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 08:17
 */
class CoordinateTest {

    static final String json = "[103.8198,1.3521]";

    @Test
    void toJson() {
        Coordinate coordinate = new Coordinate(103.8198, 1.3521);

        assertEquals(json, JsonUtils.toString(coordinate));
    }

    @Test
    void fromJson() {
        Coordinate coordinate = JsonUtils.toObject(json, Coordinate.class);

        assertEquals(coordinate.getLatitude(), 1.3521);
        assertEquals(coordinate.getLongitude(), 103.8198);
    }
}
