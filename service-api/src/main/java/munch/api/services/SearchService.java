package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.services.search.Curator;
import munch.api.services.search.CuratorDelegator;
import munch.api.services.search.cards.SearchCard;
import munch.api.services.search.cards.SearchCollection;
import munch.data.SearchQuery;
import munch.data.SearchResult;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 12/7/2017
 * Time: 12:54 PM
 * Project: munch-core
 */
@Singleton
public class SearchService extends AbstractService {

    private final SearchClient searchClient;
    private final CuratorDelegator curatorDelegator;

    @Inject
    public SearchService(SearchClient searchClient, CuratorDelegator curatorDelegator) {
        this.searchClient = searchClient;
        this.curatorDelegator = curatorDelegator;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("/suggest", this::suggest);

            // Collection Search
            POST("/collections", this::collections);
            POST("/collections/search", this::collectionsSearch);
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
    private JsonNode suggest(JsonCall call, JsonNode request) {
        int size = request.get("size").asInt();
        String text = request.get("text").asText();
        return searchClient.suggestRaw(size, text, null);
    }

    /**
     * @param call json call
     * @return list of SearchCollection containing cards
     */
    private List<SearchCollection> collections(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return curatorDelegator.delegate(query);
    }

    /**
     * @param call json call
     * @return list of Place
     * @see SearchQuery
     */
    private List<SearchCard> collectionsSearch(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        List<SearchResult> results = searchClient.search(query);
        return Curator.parseCards(results);
    }
}
