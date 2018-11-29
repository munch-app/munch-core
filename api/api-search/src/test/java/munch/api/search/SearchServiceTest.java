package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.AbstractServiceTest;
import munch.api.ApiRequest;
import munch.api.TestCase;
import munch.api.search.cards.SearchHeaderCard;
import munch.api.search.cards.SearchNoLocationCard;
import munch.api.search.cards.SearchPlaceCard;
import munch.restful.client.RestfulRequest;
import munch.restful.core.JsonUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 2:43 PM
 * Project: munch-core
 */
class SearchServiceTest extends AbstractServiceTest {

    @ParameterizedTest
    @MethodSource("searchProvider")
    void search(TestCase test) {
        RestfulRequest request = client.post("/search");
        test.asResponse(request);
    }

    @ParameterizedTest
    @MethodSource("suggestProvider")
    void suggest(TestCase test) {
        RestfulRequest request = client.post("/suggest");
        test.asResponse(request);
    }

    static Stream<TestCase> searchProvider() {
        return Stream.of(
                TestCase.of(200, "Empty with Location")
                        .request(request -> {
                            request.queryString("from", 0);
                            request.queryString("size", 10);
                            request.body(new SearchQuery());
                            request.header(ApiRequest.HEADER_USER_LAT_LNG, "1.290270, 103.851959");
                            request.header(ApiRequest.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");

                        })
                        .response(response -> {
                            ArrayNode cards = (ArrayNode) response.getDataNode();

                            int size = 0;
                            for (JsonNode card : cards) {
                                if (card.path("_cardId").asText().equals(new SearchPlaceCard().getCardId())) {
                                    size++;
                                }
                            }
                            assertThat(size).isEqualTo(10);
                        }),

                TestCase.of(200, "Empty with no Location")
                        .request(request -> {
                            request.queryString("from", 0);
                            request.queryString("size", 20);
                            request.body(new SearchQuery());
                            request.header(ApiRequest.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");
                        })
                        .response(response -> {
                            ArrayNode cards = (ArrayNode) response.getDataNode();
                            assertThat(cards.get(0).path("_cardId").asText())
                                    .isEqualTo(new SearchNoLocationCard().getCardId());
                            assertThat(cards.get(1).path("_cardId").asText())
                                    .isEqualTo(new SearchHeaderCard().getCardId());

                            int size = 0;
                            for (JsonNode card : cards) {
                                if (card.path("_cardId").asText().equals(new SearchPlaceCard().getCardId())) {
                                    size++;
                                }
                            }
                            assertThat(size).isEqualTo(20);
                        }),

                TestCase.of(200, "Area No Location")
                        .request(request -> {
                            request.queryString("from", 0);
                            request.queryString("size", 10);
                            SearchQuery areaQuery = new SearchQuery();
                            areaQuery.getFilter().setLocation(new SearchQuery.Filter.Location());
                            areaQuery.getFilter().getLocation().setAreas(List.of(SearchQueryTestUtils.SMALL_AREA));
                            request.body(areaQuery);
                            request.header(ApiRequest.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");
                        })
                        .response(response -> {
                            ArrayNode cards = (ArrayNode) response.getDataNode();
                            assertThat(cards.get(0).path("_cardId").asText())
                                    .isEqualTo(new SearchNoLocationCard().getCardId());
                        })
        );
    }

    static Stream<TestCase> suggestProvider() {
        return Stream.of(
                TestCase.of(200, "Text & Suggestion")
                        .request(request -> {
                            ObjectNode body = JsonUtils.createObjectNode();
                            body.put("text", "chicke");
                            body.set("searchQuery", JsonUtils.toTree(new SearchQuery()));
                            request.body(body);

                            request.header(ApiRequest.HEADER_USER_LAT_LNG, "1.290270, 103.851959");
                            request.header(ApiRequest.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");

                        })
                        .response(response -> {
                            JsonNode dataNode = response.getDataNode();

                            assertThat(dataNode.path("suggests").size()).isGreaterThan(2);
                            assertThat(dataNode.path("places").size()).isGreaterThan(2);
                            assertThat(dataNode.path("assumptions").size()).isZero();
                        }),

                TestCase.of(200, "Assumptions")
                        .request(request -> {
                            ObjectNode body = JsonUtils.createObjectNode();
                            body.put("text", "Chicken");
                            body.set("searchQuery", JsonUtils.toTree(new SearchQuery()));
                            request.body(body);

                            request.header(ApiRequest.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");
                        })
                        .response(response -> {
                            JsonNode dataNode = response.getDataNode();

                            assertThat(dataNode.path("suggests").size()).isGreaterThan(2);
                            assertThat(dataNode.path("places").size()).isGreaterThan(2);
                            assertThat(dataNode.path("assumptions").size()).isGreaterThan(0);
                        }),

                TestCase.of(200, "With Location")
                        .request(request -> {
                            ObjectNode body = JsonUtils.createObjectNode();
                            body.put("text", "Chinese");

                            SearchQuery areaQuery = new SearchQuery();
                            areaQuery.getFilter().setLocation(new SearchQuery.Filter.Location());
                            areaQuery.getFilter().getLocation().setAreas(List.of(SearchQueryTestUtils.SMALL_AREA));
                            request.body(areaQuery);
                            body.set("searchQuery", JsonUtils.toTree(areaQuery));
                            request.body(body);

                            request.header(ApiRequest.HEADER_USER_LAT_LNG, "1.290270, 103.851959");
                            request.header(ApiRequest.HEADER_USER_LOCAL_TIME, "2011-12-03T10:15:30");
                        })
                        .response(response -> {
                            JsonNode dataNode = response.getDataNode();

                            assertThat(dataNode.path("suggests").size()).isGreaterThan(2);
                            assertThat(dataNode.path("places").size()).isGreaterThan(2);
                        })
        );
    }
}