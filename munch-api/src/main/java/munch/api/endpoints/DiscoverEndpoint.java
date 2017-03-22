package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.search.SearchQuery;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public class DiscoverEndpoint extends MunchEndpoint {

    private final SearchQuery search;

    @Inject
    public DiscoverEndpoint(SearchQuery search) {
        this.search = search;
    }

    @Override
    public void route() {
        path("/discover", () -> {
            post("", this::discover);
        });
    }

    /**
     * @param call json call
     * @param node json node
     * @return JsonNode discover data
     */
    private JsonNode discover(JsonCall call, JsonNode node) {
        Spatial spatial = getSpatial(node);
        return null;
    }
}
