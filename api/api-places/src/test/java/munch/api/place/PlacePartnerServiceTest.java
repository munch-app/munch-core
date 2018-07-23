package munch.api.place;

import munch.api.AbstractServiceTest;
import munch.api.TestCase;
import munch.article.clients.Article;
import munch.corpus.instagram.InstagramMedia;
import munch.restful.client.RestfulRequest;
import munch.restful.client.RestfulResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 7:21 PM
 * Project: munch-core
 */
public class PlacePartnerServiceTest extends AbstractServiceTest {
    @ParameterizedTest
    @MethodSource("providerList")
    void getArticles(TestCase result) {
        RestfulRequest request = client.get("/places/:placeId/partners/articles");
        RestfulResponse response = result.asResponse(request);

        assertListClass(response.getDataNode(), Article.class);
    }

    @ParameterizedTest
    @MethodSource("providerList")
    void getInstagramMedias(TestCase result) {
        RestfulRequest request = client.get("/places/:placeId/partners/instagram/medias");
        RestfulResponse response = result.asResponse(request);

        assertListClass(response.getDataNode(), InstagramMedia.class);
    }

    static Stream<TestCase> providerList() {
        return Stream.of(
                TestCase.of(200)
                        .request(request -> {
                            request.path("placeId", "cdbd564c-45fc-439c-ba3e-8636729c1a4c");
                        })
                        .response(response -> {
                            assertThat(response.getDataNode().isArray()).isTrue();
                            assertThat(response.getDataNode().size()).isGreaterThan(0);
                        }),
                TestCase.of(200)
                        .request(request -> {
                            request.path("placeId", "f36422cb-2186-4821-a3f3-13b8cb26ebe8");
                        })
                        .response(response -> {
                            assertThat(response.getDataNode().isArray()).isTrue();
                            assertThat(response.getDataNode().size()).isZero();
                        })
        );
    }
}
