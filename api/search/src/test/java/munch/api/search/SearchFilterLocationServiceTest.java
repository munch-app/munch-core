package munch.api.search;

import munch.api.AbstractServiceTest;
import munch.data.location.Area;
import munch.restful.client.RestfulResponse;
import munch.restful.core.JsonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 2:04 PM
 * Project: munch-core
 */
class SearchFilterLocationServiceTest extends AbstractServiceTest {

    @Test
    void head() {
        RestfulResponse response = client.head("/search/filter/locations").asResponse();
        response.hasCode(200);

        String header = response.getHeader("Last-Modified-Millis");
        Assertions.assertNotNull(header);
        new Date(Long.parseLong(header));
    }

    @Test
    void get() {
        RestfulResponse response = client.get("/search/filter/locations").asResponse();
        response.hasCode(200);

        String header = response.getHeader("Last-Modified-Millis");
        Assertions.assertNotNull(header);
        Date largestDate = new Date(Long.parseLong(header));

        List<Area> areaList = JsonUtils.toList(response.getDataNode(), Area.class);
        long largest = areaList.stream()
                .max(Comparator.comparingLong(Area::getUpdatedMillis))
                .map(Area::getUpdatedMillis)
                .orElse(0L);

        Assertions.assertEquals(largestDate.getTime(), largest);
    }
}