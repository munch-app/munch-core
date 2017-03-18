package munch.restful.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import munch.restful.client.exception.ExceptionHandler;
import munch.restful.client.exception.StructuredException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 4:19 PM
 * Project: munch-core
 */
public class RestfulResponse {
    protected static final ObjectMapper mapper = RestfulClient.mapper;

    private final JsonNode jsonNode;
    private final HttpResponse<InputStream> response;

    public RestfulResponse(HttpResponse<InputStream> response) {
        this.response = response;
        try {
            this.jsonNode = mapper.readTree(response.getBody());

            // Check if meta contains exception
            JsonNode meta = jsonNode.path("meta");
            if (meta.has("errorType")) {
                String type = meta.path("errorType").asText();
                String message = meta.path("errorMessage").asText();
                throw new StructuredException(type, message);
            }
        } catch (IOException e) {
            throw ExceptionHandler.handle(e);
        }
    }

    public int getStatus() {
        return response.getStatus();
    }

    /**
     * @return Response Headers (map) with <b>same case</b> as server response.
     * For instance use <code>getHeaders().getFirst("Location")</code> and not <code>getHeaders().getFirst("location")</code> to get first header "Location"
     */
    public Headers getHeaders() {
        return response.getHeaders();
    }

    /**
     * @return get first header
     */
    public String getHeader(Object key) {
        return getHeaders().getFirst(key);
    }

    /**
     * @return json node
     */
    public JsonNode getNode() {
        return jsonNode;
    }

    /**
     * @return json node
     */
    public JsonNode getMetaNode() {
        return getNode().path("meta");
    }

    /**
     * @return json node
     */
    public JsonNode getDataNode() {
        return getNode().path("data");
    }

    public <T> T asDataObject(Class<T> clazz) {
        try {
            return mapper.treeToValue(getDataNode(), clazz);
        } catch (JsonProcessingException e) {
            throw ExceptionHandler.handle(e);
        }
    }

    public <T> List<T> asDataList(Class<T> clazz) {
        try {
            List<T> list = new ArrayList<>();
            for (JsonNode node : getDataNode()) {
                list.add(mapper.treeToValue(node, clazz));
            }
            return list;
        } catch (JsonProcessingException e) {
            throw ExceptionHandler.handle(e);
        }
    }
}
