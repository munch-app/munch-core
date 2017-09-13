package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.services.discovery.CuratorDelegator;
import munch.data.search.SearchCollection;
import munch.data.search.SearchQuery;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 12/7/2017
 * Time: 12:54 PM
 * Project: munch-core
 */
@Singleton
public class DiscoveryService extends AbstractService {

    private final SearchClient searchClient;
    private final CuratorDelegator curatorDelegator;

    @Inject
    public DiscoveryService(SearchClient searchClient, CuratorDelegator curatorDelegator) {
        this.searchClient = searchClient;
        this.curatorDelegator = curatorDelegator;
    }

    @Override
    public void route() {
        PATH("/discovery", () -> {
            POST("/suggest", this::suggest);
            POST("/search", this::search);
            POST("/search/next", this::searchNext);
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
     * @return list of Place result
     */
    private List<SearchCollection> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return curatorDelegator.delegate(query);
    }

    /**
     * @param call json call
     * @return list of Place
     * @see SearchQuery
     */
    private JsonNode searchNext(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        JsonNode data = searchClient.searchRaw(query);
        return nodes(200, data);
    }
}
