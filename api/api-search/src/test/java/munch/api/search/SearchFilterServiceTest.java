package munch.api.search;

import munch.api.AbstractServiceTest;
import munch.api.TestCase;
import munch.restful.client.RestfulRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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
        );
    }

    static Stream<TestCase> priceProvider() {
        return Stream.of(

        );
    }
}