package munch.api.search.suggest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.SearchRequest;
import munch.api.search.assumption.AssumptionEngine;
import munch.api.search.assumption.data.AssumptionQuery;
import munch.api.search.assumption.data.AssumptionQueryResult;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.api.search.elastic.ElasticSuggestUtils;
import munch.data.client.ElasticClient;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 12:03 PM
 * Project: munch-core
 */
@Singleton
public final class SuggestDelegator {
    private final ElasticClient elasticClient;
    private final AssumptionEngine assumptionEngine;

    @Inject
    public SuggestDelegator(ElasticClient elasticClient, AssumptionEngine assumptionEngine) {
        this.elasticClient = elasticClient;
        this.assumptionEngine = assumptionEngine;
    }

    public Map<String, Object> delegate(String text, SearchRequest request) {
        return Map.of(
                "suggests", suggestNames(text, request),
                "assumptions", suggestQueries(text, request),
                "places", suggestPlaces(text, request)
        );
    }

    private List<String> suggestNames(String text, SearchRequest request) {
        JsonNode results = elasticClient.search(ElasticSuggestUtils.make(text, request.getLatLng(), 6))
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

    private List<Place> suggestPlaces(String text, SearchRequest request) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 40);
        ObjectNode queryNode = root.putObject("query");
        ObjectNode boolNode = queryNode.putObject("bool");

        // must: {?}
        JsonNode must = multiMatchNameNames(text);
        String latLng = request.getLatLng();
        boolNode.set("must", withFunctionScoreMust(latLng, must));

        // filter: [{"term": {"dataType": "Place"}}]
        boolNode.putArray("filter")
                .addObject()
                .putObject("term")
                .put("dataType", "Place");

        return elasticClient.searchHitsHits(root);
    }

    private List<AssumptionQueryResult> suggestQueries(String text, SearchRequest originalRequest) {

        for (AssumptionQuery assumptionQuery : assumptionEngine.assume(originalRequest, text)) {
            SearchRequest request = originalRequest.cloneWith(assumptionQuery.getSearchQuery());

            List<Place> places = suggestPlaces(0, 20, request);
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

    private List<Place> suggestPlaces(int from, int size, SearchRequest request) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", from);
        root.put("size", size);
        root.set("query", ElasticQueryUtils.make(request));
        root.set("sort", ElasticSortUtils.make(request));
        return elasticClient.searchHitsHits(root);
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
