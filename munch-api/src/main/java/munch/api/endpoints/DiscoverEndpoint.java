package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.api.services.SearchService;
import munch.restful.server.JsonCall;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
@Singleton
public class DiscoverEndpoint extends MunchEndpoint {

    private final SearchService service;

    @Inject
    public DiscoverEndpoint(SearchService service) {
        this.service = service;
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
