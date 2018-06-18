package munch.api.search;

import munch.api.AbstractServiceTest;
import munch.api.ApiService;
import munch.api.TestCase;
import munch.api.search.data.FilterCount;
import munch.api.search.data.FilterPrice;
import munch.api.search.data.SearchQuery;
import munch.restful.client.RestfulRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 2:12 PM
 * Project: munch-core
 */
class SearchFilterServiceTest extends AbstractServiceTest {

    @ParameterizedTest
    @MethodSource("countProvider")
    void count(TestCase test) {
        RestfulRequest request = client.post("/search/filter/count");
        test.asResponse(request);
    }

    @ParameterizedTest
    @MethodSource("priceProvider")
    void price(TestCase test) {
        RestfulRequest request = client.post("/search/filter/price");
        test.asResponse(request);
    }

    static Stream<TestCase> countProvider() {
        return Stream.of(
                TestCase.of(200, "Empty")
                        .request(request -> {
                            request.body(new SearchQuery());
                        })
                        .response(response -> {
                            FilterCount count = response.asDataObject(FilterCount.class);
                            assertThat(count.getCount()).isGreaterThan(100);

                            int max = count.getTags().entrySet().stream()
                                    .max(Comparator.comparingInt(Map.Entry::getValue))
                                    .map(Map.Entry::getValue)
                                    .orElse(0);

                            assertThat(max).isGreaterThanOrEqualTo(100);
                        }),
                TestCase.of(200, "Empty with user location")
                        .request(request -> {
                            request.body(new SearchQuery());
                            request.header(ApiService.HEADER_USER_LAT_LNG, "1.290270, 103.851959");
                            request.header(ApiService.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");
                        })
                        .response(response -> {
                            assertThat(response.asDataObject(FilterCount.class).getCount())
                                    .isGreaterThan(0)
                                    .isLessThan(100);
                        }),
                TestCase.of(200, "Area")
                        .request(request -> {
                            SearchQuery areaQuery = new SearchQuery();
                            areaQuery.getFilter().setArea(SearchQueryTestUtils.SMALL_AREA);
                            request.body(areaQuery);
                        })
                        .response(response -> {
                            FilterCount count = response.asDataObject(FilterCount.class);
                            int max = count.getTags().entrySet().stream()
                                    .max(Comparator.comparingInt(Map.Entry::getValue))
                                    .map(Map.Entry::getValue)
                                    .orElse(Integer.MAX_VALUE);

                            assertThat(max).isLessThan(100);
                        })
        );
    }

    static Stream<TestCase> priceProvider() {
        return Stream.of(
                TestCase.of(200, "Empty")
                        .request(request -> {
                            request.body(new SearchQuery());
                        })
                        .response(response -> {
                            FilterPrice price = response.asDataObject(FilterPrice.class);

                            int sum = price.getFrequency().values().stream()
                                    .mapToInt(value -> value)
                                    .sum();
                            assertThat(sum).isGreaterThanOrEqualTo(1500);
                        }),
                TestCase.of(200, "Area")
                        .request(request -> {
                            SearchQuery areaQuery = new SearchQuery();
                            areaQuery.getFilter().setArea(SearchQueryTestUtils.SMALL_AREA);
                            request.body(areaQuery);

                            request.header(ApiService.HEADER_USER_LAT_LNG, "1.290270, 103.851959");
                            request.header(ApiService.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");
                        })
                        .response(response -> {
                            FilterPrice price = response.asDataObject(FilterPrice.class);

                            int sum = price.getFrequency().values().stream()
                                    .mapToInt(value -> value)
                                    .sum();
                            assertThat(sum).isLessThan(1000);
                        })
        );
    }
}