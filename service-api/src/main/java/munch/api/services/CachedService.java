package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by: Fuxing
 * Date: 2/7/2017
 * Time: 3:16 AM
 * Project: munch-core
 */
@Singleton
public class CachedService extends AbstractService {

    private final StaticJson jsonResource;
    private final ObjectNode hashesNodes;

    private final JsonNode popularLocations;

    @Inject
    public CachedService(StaticJson jsonResource) throws IOException {
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

    /**
     * Read from resources/static-json
     * <p>
     * Created by: Fuxing
     * Date: 14/7/2017
     * Time: 11:30 PM
     * Project: munch-core
     */
    @Singleton
    public static final class StaticJson {
        private static final String directory = "static-json";
        private final ObjectMapper objectMapper;

        @Inject
        public StaticJson(ObjectMapper objectMapper) throws IOException {
            this.objectMapper = objectMapper;
        }

        /**
         * @param resource resource name with extension
         * @return root JsonNode
         * @throws IOException json reading error
         */
        public JsonNode getResource(String resource) throws IOException {
            URL url = Resources.getResource(directory + "/" + resource);
            return objectMapper.readTree(url);
        }

        /**
         * @param resource resource name with extension
         * @param tClass   Class Type
         * @param <T>      Type
         * @return Typed Object
         * @throws IOException json reading error
         */
        public <T> T getResource(String resource, Class<T> tClass) throws IOException {
            URL url = Resources.getResource(directory + "/" + resource);
            return objectMapper.readValue(url, tClass);
        }
    }
}
