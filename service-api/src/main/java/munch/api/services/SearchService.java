package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.data.assumption.AssumedSearchQuery;
import munch.data.assumption.AssumptionEngine;
import munch.data.clients.PlaceClient;
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
    private final PlaceClient.SearchClient searchClient;

    @Inject
    public SearchService(AssumptionEngine assumptionEngine, PlaceClient.SearchClient searchClient) {
        this.assumptionEngine = assumptionEngine;
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
        final SearchQuery prevQuery = JsonUtils.toObject(request.path("query"), SearchQuery.class);

        List<AssumptionQueryResult> assumptions = new ArrayList<>();
        for (AssumedSearchQuery query : assumptionEngine.assume(prevQuery, text)) {
            List<Place> places = searchClient.search(query.getSearchQuery());
            if (!places.isEmpty()) {
                AssumptionQueryResult result = new AssumptionQueryResult();
                result.setPlaces(places);
                result.setTokens(query.getTokens());
                assumptions.add(result);
                break;
            }
        }

        List<Place> places = searchClient.search(prevQuery);

        return Map.of(
                "assumptions", JsonUtils.toTree(assumptions),
                "places", JsonUtils.toTree(places)
        );
    }

    public static class AssumptionQueryResult {
        private List<AssumedSearchQuery.Token> tokens;
        private List<Place> places;

        public List<AssumedSearchQuery.Token> getTokens() {
            return tokens;
        }

        public void setTokens(List<AssumedSearchQuery.Token> tokens) {
            this.tokens = tokens;
        }

        public List<Place> getPlaces() {
            return places;
        }

        public void setPlaces(List<Place> places) {
            this.places = places;
        }
    }
}
