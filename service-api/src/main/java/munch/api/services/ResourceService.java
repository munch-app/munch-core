package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.StaticJsonResource;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 2/7/2017
 * Time: 3:16 AM
 * Project: munch-core
 */
@Singleton
public class ResourceService extends AbstractService {

    private final StaticJsonResource jsonResource;

    @Inject
    public ResourceService(StaticJsonResource jsonResource) throws IOException {
        this.jsonResource = jsonResource;
    }

    @Override
    public void route() {
        PATH("/resources", () -> {
            PATH("/popular-locations", () -> pathResources(getResource("popular-locations")));
        });
    }

    private void pathResources(JsonNode node) {
        String hash = DigestUtils.sha512Hex(node.toString());

        GET("/hash", call -> nodes(200, objectMapper.createObjectNode()
                .put("hash", hash)));
        GET("", call -> nodes(200, objectMapper.createObjectNode()
                .put("hash", hash)
                .set("data", node)));
    }

    private JsonNode getResource(String resourceName) {
        try {
            return jsonResource.getResource(resourceName + ".json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
