package app.munch.model;

import dev.fuxing.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Fuxing Loh
 * @since 2019-11-25 at 14:55
 */
class PlaceTest {

    @Test
    void toJson() {
        Place place = new Place();
        String json = JsonUtils.toString(place);

        assertEquals("{}", json);
    }
}
