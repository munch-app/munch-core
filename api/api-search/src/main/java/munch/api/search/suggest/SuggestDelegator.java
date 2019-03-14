package munch.api.search.suggest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.search.SearchRequest;
import munch.api.search.assumption.AssumptionEngine;
import munch.api.search.assumption.data.AssumptionQuery;
import munch.api.search.assumption.data.AssumptionQueryResult;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.api.search.elastic.ElasticSortUtils;
import munch.data.client.ElasticClient;
import munch.data.elastic.ElasticObject;
import munch.data.elastic.SuggestObject;
import munch.data.location.Country;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        // In future updates, return the current results as it is
        List<ElasticObject> objects = elasticClient.suggest(Country.SGP, text, request.getLatLng(), 40);
        List<String> names = extractNames(objects);
        List<Place> places = extractPlaces(objects);

        List<AssumptionQueryResult> assumptions = suggestAssumption(text, names, request);
        return Map.of(
                "suggests", names,
                "places", places,
                "assumptions", assumptions
        );
    }

    private static List<String> extractNames(List<ElasticObject> objects) {
        List<String> names = new ArrayList<>();
        for (ElasticObject object : objects) {
            if (object instanceof SuggestObject) {
                names.add(((SuggestObject) object).getName());
            }
        }
        return names;
    }

    private static List<Place> extractPlaces(List<ElasticObject> objects) {
        List<Place> places = new ArrayList<>();
        for (ElasticObject object : objects) {
            if (object instanceof Place) {
                places.add((Place) object);
            }
        }
        return places;
    }

    /**
     * TODO(fuxing): needs improvement
     */
    @SuppressWarnings({"Duplicates", "LoopStatementThatDoesntLoop"})
    private List<AssumptionQueryResult> suggestAssumption(String text, List<String> names, SearchRequest originalRequest) {
        for (AssumptionQuery assumptionQuery : assumptionEngine.assume(originalRequest, text)) {
            AssumptionQueryResult result = query(originalRequest, assumptionQuery);
            if (result != null) return List.of(result);
            return List.of();
        }

        for (String name : names) {
            for (AssumptionQuery assumptionQuery : assumptionEngine.assume(originalRequest, name)) {
                AssumptionQueryResult result = query(originalRequest, assumptionQuery);
                if (result != null) return List.of(result);
                return List.of();
            }
        }

        return List.of();
    }

    private AssumptionQueryResult query(SearchRequest originalRequest, AssumptionQuery assumptionQuery) {
        SearchRequest request = originalRequest.cloneWith(assumptionQuery.getSearchQuery());

        List<Place> places = suggestPlaces(0, 4, request);
        if (places.isEmpty()) return null;

        AssumptionQueryResult result = new AssumptionQueryResult();
        result.setPlaces(places);
        result.setSearchQuery(assumptionQuery.getSearchQuery());
        result.setTokens(assumptionQuery.getTokens());

        ObjectNode root = JsonUtils.createObjectNode();
        root.set("query", ElasticQueryUtils.make(request));
        Long count = elasticClient.count(root);
        result.setCount(count == null ? 0 : count);

        return result;
    }

    private List<Place> suggestPlaces(int from, int size, SearchRequest request) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", from);
        root.put("size", size);
        root.set("query", ElasticQueryUtils.make(request));
        root.set("sort", ElasticSortUtils.make(request));
        return elasticClient.searchHitsHits(root);
    }
}
