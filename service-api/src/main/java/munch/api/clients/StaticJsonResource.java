package munch.api.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.net.URL;

/**
 * Read from resources/static-json
 * <p>
 * Created by: Fuxing
 * Date: 14/7/2017
 * Time: 11:30 PM
 * Project: munch-core
 */
@Singleton
public final class StaticJsonResource {
    private final ObjectMapper objectMapper;

    @Inject
    public StaticJsonResource(ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
    }

    /**
     * @param resource resource name with extension
     * @return root JsonNode
     * @throws IOException json reading error
     */
    public JsonNode getResource(String resource) throws IOException {
        URL url = Resources.getResource("static-json/" + resource);
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
        URL url = Resources.getResource("static-json/" + resource);
        return objectMapper.readValue(url, tClass);
    }
}
