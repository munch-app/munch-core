package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.cached.StaticJsonResource;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 2/7/2017
 * Time: 3:16 AM
 * Project: munch-core
 */
@Singleton
public class CachedSyncService extends AbstractService {

    private final StaticJsonResource jsonResource;
    private final ObjectNode hashesNodes;

    private final JsonNode popularLocations;

    @Inject
    public CachedSyncService(StaticJsonResource jsonResource) throws IOException {
        this.jsonResource = jsonResource;
        this.hashesNodes = objectMapper.createObjectNode();

        this.popularLocations = createJsonHashNode("popular-locations");
    }

    @Override
    public void route() {
        PATH("/cached", () -> {
            // {"data": { "popular-locations": "hash-value" }}
            GET("/hashes", call -> nodes(200, hashesNodes));

            // Returns: "data": { "hash": "", "data": data }
            PATH("/data", () -> {
                GET("/popular-locations", call -> nodes(200, popularLocations));
            });
        });
    }

    private JsonNode createJsonHashNode(String resourceName) throws IOException {
        JsonNode node = jsonResource.getResource(resourceName + ".json");
        String hash = DigestUtils.sha512Hex(node.toString());

        // Update to hashes node
        hashesNodes.put(resourceName, hash);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("hash", hash);
        objectNode.set("data", node);
        return objectNode;
    }
}
