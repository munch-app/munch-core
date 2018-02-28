package munch.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.search.SearchManager;
import munch.api.services.search.cards.SearchCard;
import munch.data.structure.SearchQuery;
import munch.data.structure.SearchResult;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;

import java.util.HashMap;
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

    private final TokenAuthenticator authenticator;

    private final SearchManager searchManager;

    @Inject
    public SearchService(TokenAuthenticator authenticator, SearchManager searchManager) {
        this.authenticator = authenticator;
        this.searchManager = searchManager;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("", this::search);
            POST("/count", this::count);

            POST("/suggest", this::suggest);
            POST("/suggest/place", this::suggestPlace);
            POST("/suggest/price/range", this::suggestPriceRange);
        });
    }

    /**
     * <pre>
     *  {
     *      "types": {"Location": 5, "Place": 20},
     *      "text": "",
     *      "latLng": "" // Optional
     *  }
     * </pre>
     *
     * @param call json call
     * @return Map of (type: List of SearchResult)
     */
    private Map<String, Object> suggest(JsonCall call) throws JsonProcessingException {
        JsonNode request = call.bodyAsJson();
        final String latLng = request.path("latLng").asText(null);
        final String text = ParamException.requireNonNull("text", request.get("text").asText());
        final JsonNode typesNode = ParamException.requireNonNull("types", request.get("types"));
        final SearchQuery prevQuery = objectMapper.treeToValue(request.path("query"), SearchQuery.class);

        Map<String, Integer> types = new HashMap<>();
        typesNode.fields().forEachRemaining(e -> types.put(e.getKey(), e.getValue().asInt()));

        return searchManager.suggest(types, text, latLng, prevQuery);
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private List<SearchCard> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        String userId = authenticator.optional(call).map(AuthenticatedToken::getSubject).orElse(null);
        return searchManager.search(query, userId);
    }

    /**
     * @param call json call
     * @return total possible place result count
     */
    private Long count(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return searchManager.count(query);
    }

    /**
     * @param call json call with search query
     * @return price range aggregations
     */
    private JsonNode suggestPriceRange(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return nodes(200, searchManager.priceAggregation(query));
    }

    private List<SearchResult> suggestPlace(JsonCall call) {
        JsonNode request = call.bodyAsJson();
        final int from = request.path("from").asInt(0);
        final int size = request.path("size").asInt(20);
        final String latLng = request.path("latLng").asText(null);
        final String text = ParamException.requireNonNull("text", request.get("text").asText());

        return searchManager.suggestPlace(text, latLng, from, size);
    }
}
