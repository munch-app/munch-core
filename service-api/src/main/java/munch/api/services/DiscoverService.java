package munch.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import munch.api.services.search.SearchManager;
import munch.api.services.search.cards.SearchCard;
import munch.data.structure.SearchQuery;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 15/3/18
 * Time: 10:20 PM
 * Project: munch-core
 */
@Singleton
public final class DiscoverService extends AbstractService {

    private final TokenAuthenticator authenticator;
    private final SearchManager searchManager;

    @Inject
    public DiscoverService(TokenAuthenticator authenticator, SearchManager searchManager) {
        this.authenticator = authenticator;
        this.searchManager = searchManager;
    }

    @Override
    public void route() {
        PATH("/discover", () -> {
            POST("", this::discover);

            POST("/filter/count", this::filterCount);
            POST("/filter/suggest", this::filterSuggest);
            POST("/filter/price/range", this::filterPriceRange);
        });
    }

    /**
     * @param call json call with search query
     * @return list of Place
     * @see SearchQuery
     */
    private List<SearchCard> discover(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        String userId = authenticator.optional(call).map(AuthenticatedToken::getSubject).orElse(null);
        return searchManager.search(query, userId);
    }

    /**
     * @param call json call with search query
     * @return price range aggregations
     */
    private JsonNode filterPriceRange(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return nodes(200, searchManager.priceAggregation(query));
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
    private Map<String, Object> filterSuggest(JsonCall call) throws JsonProcessingException {
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
     * @param call json call
     * @return total possible place result count
     */
    private Long filterCount(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return searchManager.count(query);
    }
}
