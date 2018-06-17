package munch.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;
import munch.restful.client.RestfulClient;
import munch.restful.client.RestfulRequest;
import munch.restful.core.JsonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 1:31 AM
 * Project: munch-core
 */
public class AbstractServiceTest {
    protected static Client client;

    @BeforeAll
    static void setUp() {
        client = new Client();
    }

    public static void assertPath(JsonNode node, String path) {
        Assertions.assertTrue(node.has(path));
    }

    public static <T> void assertClass(JsonNode dataNode, String path, Class<T> clazz) {
        JsonNode data = dataNode.path(path);
        assertClass(data, clazz);
    }

    public static <T> void assertClass(JsonNode dataNode, Class<T> clazz) {
        if (dataNode.isMissingNode()) return;
        JsonUtils.validate(dataNode, clazz);
    }

    public static <T> void assertListClass(JsonNode dataNode, Class<T> clazz) {
        for (JsonNode node : dataNode) {
            JsonUtils.validate(node, clazz);
        }
    }

    public static class Client extends RestfulClient {
        public Client() {
            super(ConfigFactory.load().getString("test.api.url"));
        }

        public RestfulRequest get(String path) {
            return doGet(path);
        }

        public RestfulRequest post(String path) {
            return doPost(path);
        }

        public RestfulRequest put(String path) {
            return doPut(path);
        }

        public RestfulRequest delete(String path) {
            return doDelete(path);
        }

        public RestfulRequest head(String path) {
            return doHead(path);
        }

        public RestfulRequest options(String path) {
            return doOptions(path);
        }

        public RestfulRequest patch(String path) {
            return doPatch(path);
        }
    }
}
