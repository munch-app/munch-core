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
    private final SearchManager searchManager;

    @Inject
    public SearchService(SearchClient searchClient, SearchManager searchManager) {
        this.searchClient = searchClient;
        this.searchManager = searchManager;
    }

    @Override
    public void route() {
        PATH("/search", () -> {
            POST("/suggest", this::suggest);
            POST("/search", this::search);
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
    private List<SearchResult> suggest(JsonCall call, JsonNode request) {
        int size = request.get("size").asInt();
        String text = request.get("text").asText();
        return searchClient.suggest(text, null, size);
    }

    /**
     * @param call json call
     * @return list of Place
     * @see SearchQuery
     */
    private List<SearchCard> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return searchManager.search(query);
    }
}
