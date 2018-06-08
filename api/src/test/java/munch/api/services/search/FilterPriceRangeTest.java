package munch.api.services.search;

import munch.restful.core.JsonUtils;
import org.junit.jupiter.api.Test;

/**
 * Created by: Fuxing
 * Date: 18/4/2018
 * Time: 1:00 AM
 * Project: munch-core
 */
class FilterPriceRangeTest {

    @Test
    void nanParsing() throws Exception {
        FilterPriceRange.Segment segment = new FilterPriceRange.Segment(Double.NaN, Double.NaN);
        System.out.println(JsonUtils.toString(segment));
    }
}