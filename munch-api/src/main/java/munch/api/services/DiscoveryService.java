package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.SearchClient;
import munch.api.data.LatLng;
import munch.api.data.SearchCollection;
import munch.api.data.SearchQuery;
import munch.api.services.curator.CuratorDelegator;
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
        String latLng = request.path("latLng").asText(null); // Nullable
        JsonNode data = searchClient.suggestRaw(size, text, latLng);
        return nodes(200, data);
    }

    /**
     * @param call json call
     * @return list of Place result
     */
    private JsonNode search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        LatLng latLng = getHeaderLatLng(call).orElse(null);
        List<SearchCollection> collections = curatorDelegator.delegate(query, latLng);

        ObjectNode nodes = nodes(200, collections);
        // Return query object to keep search bar concurrent
        nodes.set("query", toTree(query));
        return nodes;
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
