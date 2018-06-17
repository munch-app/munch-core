package munch.api.place;

import munch.api.AbstractServiceTest;
import munch.api.TestResult;
import munch.article.clients.Article;
import munch.data.place.Place;
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
    void get(TestResult result) {
        RestfulResponse response = client.get("/places/:placeId")
                .path("placeId", result.params.get("placeId"))
                .asResponse();

        result.validate(response);
        assertClass(response.getDataNode(), Place.class);
    }

    @ParameterizedTest
    @MethodSource("provider")
    void getCards(TestResult result) {
        RestfulResponse response = client.get("/places/:placeId/cards")
                .path("placeId", result.params.get("placeId"))
                .asResponse();

        result.validate(response);

        if (result.code == 200) {
            assertClass(response.getDataNode(), "place", Place.class);
            assertPath(response.getDataNode(), "cards");
        }
    }

    @ParameterizedTest
    @MethodSource("provider")
    void getArticles(TestResult result) {
        RestfulResponse response = client.get("/places/:placeId/partners/articles")
                .path("placeId", result.params.get("placeId"))
                .asResponse();

        assertListClass(response.getDataNode(), Article.class);
    }

    @ParameterizedTest
    @MethodSource("provider")
    void getInstagramMedias(TestResult result) {
        RestfulResponse response = client.get("/places/:placeId/partners/instagram/medias")
                .path("placeId", result.params.get("placeId"))
                .asResponse();

        assertListClass(response.getDataNode(), Article.class);
    }

    static Stream<TestResult> provider() {
        return Stream.of(
                TestResult.of(200, "placeId", "f36422cb-2186-4821-a3f3-13b8cb26ebe7"),
                TestResult.of(404, "placeId", "f36422cb-2186-4821-a3f3-13b8cb26ebe8")
        );
    }
}
