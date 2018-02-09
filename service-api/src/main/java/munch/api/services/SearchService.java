package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.search.SearchManager;
import munch.api.services.search.cards.SearchCard;
import munch.data.clients.SearchClient;
import munch.data.structure.SearchQuery;
import munch.data.structure.SearchResult;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.AuthenticatedToken;
import munch.restful.server.jwt.TokenAuthenticator;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 12/7/2017
 * Time: 12:54 PM
 * Project: munch-core
 */
@Singleton
public class SearchService extends AbstractService {
    private final TokenAuthenticator authenticator;

    private final SearchClient searchClient;
    private final SearchManager searchManager;

    @Inject
    public SearchService(TokenAuthenticator authenticator, SearchClient searchClient, SearchManager searchManager) {
        this.authenticator = authenticator;
        this.searchClient = searchClient;
        this.searchManager = searchManager;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("/suggest", this::suggest);
            POST("/search", this::search);
            POST("/count", this::count);

            POST("/filter/price_range", this::filterPriceRange);
        });
    }

    /**
     * <pre>
     *  {
     *      size: 10,
     *      text: "",
     *      latLng: "" // Optional
     *  }
     * </pre>
     *
     * @param call json call
     * @return list of Place result
     */
    private List<SearchResult> suggest(JsonCall call) {
        JsonNode request = call.bodyAsJson();
        int size = request.get("size").asInt();
        String text = request.get("text").asText();
        String latLng = request.path("latLng").asText(null);
        return searchClient.suggest(text, latLng, size);
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
    private JsonNode filterPriceRange(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return nodes(200, searchManager.priceAggregation(query));
    }
}
