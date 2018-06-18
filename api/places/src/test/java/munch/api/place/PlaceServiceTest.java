package munch.api.place;

import munch.api.AbstractServiceTest;
import munch.api.TestCase;
import munch.data.place.Place;
import munch.restful.client.RestfulRequest;
import munch.restful.client.RestfulResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 2:36 AM
 * Project: munch-core
 */
public class PlaceServiceTest extends AbstractServiceTest {

    @ParameterizedTest
    @MethodSource("provider")
    void get(TestCase result) {
        RestfulRequest request = client.get("/places/:placeId");
        RestfulResponse response = result.asResponse(request);

        if (result.isOk()) {
            assertClass(response.getDataNode(), Place.class);
        }
    }

    @ParameterizedTest
    @MethodSource("provider")
    void getCards(TestCase result) {
        RestfulRequest request = client.get("/places/:placeId/cards");
        RestfulResponse response = result.asResponse(request);

        if (result.isOk()) {
            assertClass(response.getDataNode().path("place"), Place.class);
            assertHas(response.getDataNode().path("cards"));
        }
    }

    static Stream<TestCase> provider() {
        return Stream.of(
                TestCase.of(200)
                        .request(request -> {
                            request.path("placeId", "cdbd564c-45fc-439c-ba3e-8636729c1a4c");
                        }),
                TestCase.of(404)
                        .request(request -> {
                            request.path("placeId", "f36422cb-2186-4821-a3f3-13b8cb26ebe8");
                        })
        );
    }
}
