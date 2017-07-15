package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.cached.CachedManager;
import munch.restful.server.JsonCall;

/**
 * Created by: Fuxing
 * Date: 2/7/2017
 * Time: 3:16 AM
 * Project: munch-core
 */
@Singleton
public class CachedSyncService extends AbstractService {

    private final CachedManager cachedManager;
    private final ObjectNode hashesNodes;

    @Inject
    public CachedSyncService(CachedManager cachedManager, ObjectMapper objectMapper) {
        this.cachedManager = cachedManager;
        this.hashesNodes = objectMapper.createObjectNode()
                .put("popular-locations", cachedManager.getPopularLocations().getHash());
    }

    @Override
    public void route() {
        PATH("/cached", () -> {
            GET("/hashes", this::hashes);

            // Returns: "data": { "hash": "", "data": data }
            PATH("/data", () -> {
                GET("/popular-locations", call -> cachedManager.getPopularLocations());
            });
        });
    }

    /**
     * @param call json call
     * @return {"data": { "popular-locations": "hash-value" }}
     */
    private JsonNode hashes(JsonCall call) {
        return nodes(200, hashesNodes);
    }
}
