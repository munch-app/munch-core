package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.searchbox.core.Search;
import munch.data.assumption.AssumptionEngine;
import munch.data.assumption.AssumptionQuery;
import munch.data.assumption.AssumptionToken;
import munch.data.clients.PlaceClient;
import munch.data.clients.SearchClient;
import munch.data.elastic.ElasticClient;
import munch.data.structure.Place;
import munch.data.structure.SearchQuery;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 12/7/2017
 * Time: 12:54 PM
 * Project: munch-core
 */
@Singleton
public class SearchService extends AbstractService {

    private final AssumptionEngine assumptionEngine;
    private final PlaceClient.SearchClient placeSearchClient;
    private final SearchClient searchClient;

    @Inject
    public SearchService(AssumptionEngine assumptionEngine, PlaceClient.SearchClient placeSearchClient, SearchClient searchClient) {
        this.assumptionEngine = assumptionEngine;
        this.placeSearchClient = placeSearchClient;
        this.searchClient = searchClient;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("", this::search);
        });
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private Map<String, JsonNode> search(JsonCall call) {
        JsonNode request = call.bodyAsJson();
        final String text = ParamException.requireNonNull("text", request.get("text").asText());
        final String latLng = request.path("latLng").asText(null);
        final SearchQuery prevQuery = JsonUtils.toObject(request.path("query"), SearchQuery.class);

        List<AssumptionQueryResult> assumptions = new ArrayList<>();
        for (AssumptionQuery query : assumptionEngine.assume(prevQuery, text)) {
            List<Place> places = placeSearchClient.search(query.getSearchQuery());
            if (!places.isEmpty()) {
                AssumptionQueryResult result = new AssumptionQueryResult();
                result.setSearchQuery(query.getSearchQuery());
                result.setPlaces(places);
                result.setTokens(query.getTokens());
                result.setCount(placeSearchClient.count(query.getSearchQuery()));
                assumptions.add(result);
                break;
            }
        }

        Search search = ElasticClient.createSearch(List.of("Place"), List.of("name^2", "allNames"), text, latLng, 0, 40);
        List<Place> places = searchClient.search(search);
        List<String> suggests = searchClient.suggestText(text, 6);

        return Map.of(
                "suggests", JsonUtils.toTree(suggests),
                "assumptions", JsonUtils.toTree(assumptions),
                "places", JsonUtils.toTree(places)
        );
    }

    public static class AssumptionQueryResult {
        private SearchQuery searchQuery;
        private List<AssumptionToken> tokens;
        private List<Place> places;
        private long count;

        public SearchQuery getSearchQuery() {
            return searchQuery;
        }

        public void setSearchQuery(SearchQuery searchQuery) {
            this.searchQuery = searchQuery;
        }

        public List<AssumptionToken> getTokens() {
            return tokens;
        }

        public void setTokens(List<AssumptionToken> tokens) {
            this.tokens = tokens;
        }

        public List<Place> getPlaces() {
            return places;
        }

        public void setPlaces(List<Place> places) {
            this.places = places;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}
