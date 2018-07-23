package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.ApiService;
import munch.api.search.assumption.AssumptionEngine;
import munch.api.search.assumption.AssumptionQuery;
import munch.api.search.assumption.AssumptionQueryResult;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.api.search.elastic.ElasticSuggestUtils;
import munch.data.client.ElasticClient;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserSearchHistoryClient;
import munch.user.data.UserSearchHistory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 15/3/18
 * Time: 10:20 PM
 * Project: munch-core
 */
@Singleton
public final class SearchService extends ApiService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private final ElasticClient elasticClient;
    private final AssumptionEngine assumptionEngine;

    private final SearchRequest.Factory searchRequestFactory;
    private final SearchRequest.Delegator searchRequestDelegator;

    private final UserSearchHistoryClient historyClient;

    @Inject
    public SearchService(ElasticClient elasticClient, AssumptionEngine assumptionEngine, SearchRequest.Factory searchRequestFactory, SearchRequest.Delegator searchRequestDelegator, UserSearchHistoryClient historyClient) {
        this.elasticClient = elasticClient;
        this.assumptionEngine = assumptionEngine;
        this.searchRequestFactory = searchRequestFactory;
        this.searchRequestDelegator = searchRequestDelegator;
        this.historyClient = historyClient;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("", this::search);
            POST("/suggest", this::suggest);
        });
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private JsonResult search(JsonCall call) {
        SearchRequest searchRequest = searchRequestFactory.create(call);

        // Save UserSearchHistory
        CompletableFuture.runAsync(() -> {
            String userId = searchRequest.getUserId();
            if (userId == null) return;

            try {
                UserSearchHistory history = UserSearchHistory.from(userId, System.currentTimeMillis(), searchRequest.getSearchQuery());
                historyClient.put(history.getUserId(), history.getCreatedMillis(), history);
            } catch (Exception e) {
                logger.warn("Failed to persist SearchHistory userId: {}, history: {}", userId, searchRequest.getSearchQuery(), e);
            }
        });
        return searchRequestDelegator.delegate(searchRequest);
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private Map<String, Object> suggest(JsonCall call) {
        JsonNode request = call.bodyAsJson();
        String text = ParamException.requireNonNull("text", request.path("text").asText());
        SearchQuery query = JsonUtils.toObject(request.path("searchQuery"), SearchQuery.class);

        return Map.of(
                "suggests", suggestNames(text, 6, call),
                "assumptions", assumeQuery(query, text, call),
                "places", searchPlaces(0, 40, text, call)
        );
    }

    private List<Place> searchPlaces(int from, int size, SearchRequest request) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", from);
        root.put("size", size);
        root.set("query", ElasticQueryUtils.make(request));
        root.set("sort", ElasticSortUtils.make(request));
        return elasticClient.searchHitsHits(root);
    }

    private List<Place> searchPlaces(int from, int size, String text, JsonCall call) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", from);
        root.put("size", size);
        ObjectNode queryNode = root.putObject("query");
        ObjectNode boolNode = queryNode.putObject("bool");

        // must: {?}
        JsonNode must = multiMatchNameNames(text);
        String latLng = optionalUserLatLng(call).orElse(null);
        boolNode.set("must", withFunctionScoreMust(latLng, must));

        // filter: [{"term": {"dataType": "Place"}}]
        boolNode.putArray("filter")
                .addObject()
                .putObject("term")
                .put("dataType", "Place");

        return elasticClient.searchHitsHits(root);
    }

    private List<String> suggestNames(String text, int size, JsonCall call) {
        String latLng = optionalUserLatLng(call).orElse(null);

        JsonNode results = elasticClient.search(ElasticSuggestUtils.make(text, latLng, size))
                .path("suggest")
                .path("suggestions")
                .path(0)
                .path("options");
        if (results.isMissingNode()) return List.of();

        Set<String> texts = new HashSet<>();
        for (JsonNode result : results) {
            String name = result.path("_source").path("name").asText();
            if (StringUtils.isBlank(name)) continue;
            if (text.equalsIgnoreCase(name)) continue;
            texts.add(name);
        }

        return texts.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
    }

    private List<AssumptionQueryResult> assumeQuery(SearchQuery query, String text, JsonCall call) {
        SearchRequest originalRequest = searchRequestFactory.create(call, query);
        for (AssumptionQuery assumptionQuery : assumptionEngine.assume(originalRequest, text)) {
            SearchRequest request = searchRequestFactory.create(call, assumptionQuery.getSearchQuery());

            List<Place> places = searchPlaces(0, 20, request);
            if (places.isEmpty()) continue;

            AssumptionQueryResult result = new AssumptionQueryResult();
            result.setPlaces(places);
            result.setSearchQuery(assumptionQuery.getSearchQuery());
            result.setTokens(assumptionQuery.getTokens());

            ObjectNode root = JsonUtils.createObjectNode();
            root.set("query", ElasticQueryUtils.make(request));
            Long count = elasticClient.count(root);
            result.setCount(count == null ? 0 : count);

            return List.of(result);
        }

        return List.of();
    }

    private static JsonNode multiMatchNameNames(String query) {
        ObjectNode root = JsonUtils.createObjectNode();
        ObjectNode match = root.putObject("multi_match");

        match.put("query", query);
        match.put("type", "phrase_prefix");
        match.putArray("fields")
                .add("name")
                .add("names");
        return root;
    }

    private static JsonNode withFunctionScoreMust(@Nullable String latLng, JsonNode must) {
        ObjectNode root = JsonUtils.createObjectNode();
        ObjectNode function = root.putObject("function_score");
        function.put("score_mode", "multiply");
        function.set("query", must);
        ArrayNode functions = function.putArray("functions");
        functions.addObject()
                .putObject("gauss")
                .putObject("ranking")
                .put("scale", "1000")
                .put("origin", "2000");

        if (latLng != null) {
            functions.addObject()
                    .putObject("gauss")
                    .putObject("location.latLng")
                    .put("scale", "3km")
                    .put("origin", latLng);
        }
        return root;
    }
}
