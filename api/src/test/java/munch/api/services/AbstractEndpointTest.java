package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.AbstractModule;
import munch.api.ApiService;
import munch.api.ApiTestServer;
import munch.restful.client.RestfulClient;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:33 PM
 * Project: munch-core
 */
public abstract class AbstractEndpointTest extends RestfulClient
        implements ApiTestServer {

    protected static final ObjectMapper mapper = new ObjectMapper();

    public <T extends ApiService> AbstractEndpointTest(Class<T> type, AbstractModule... modules) {
        super("http://localhost:8888/v1");
        ApiTestServer.start(type, modules);
    }

    protected JsonNode spatialNode(double lat, double lng) {
        ObjectNode spatial = mapper.createObjectNode();
        spatial.put("lat", lat);
        spatial.put("lng", lng);
        return spatial;
    }
}
