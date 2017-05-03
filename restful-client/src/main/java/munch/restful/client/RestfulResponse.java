package munch.restful.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import munch.restful.client.exception.ExceptionParser;
import munch.restful.client.exception.StructuredException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

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

    RestfulResponse(HttpResponse<InputStream> response) {
        this.response = response;
        try {
            this.jsonNode = mapper.readTree(response.getBody());
        } catch (IOException e) {
            throw ExceptionParser.handle(e);
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
     * @return error type from meta node
     */
    @Nullable
    public String getErrorType() {
        return getMetaNode().path("errorType").asText(null);
    }

    /**
     * If no error type found, will always return false
     *
     * @param types array of error type
     * @return true if contain a error type and is equal to given type
     */
    public boolean hasErrorType(String... types) {
        String errorType = getErrorType();
        if (errorType == null) return false;

        for (String type : types) {
            if (errorType.equals(type)) return true;
        }
        return false;
    }

    /**
     * @return json node
     */
    public JsonNode getDataNode() {
        return getNode().path("data");
    }

    /**
     * Check that meta json has follow codes
     * if not structured error will be thrown
     *
     * @param codes allowed codes in meta
     * @return RestfulResponse
     * @throws StructuredException if codes are not matched
     */
    public RestfulResponse hasMetaCodes(int... codes) throws StructuredException {
        JsonNode meta = jsonNode.path("meta");
        int code = meta.path("code").asInt();
        for (int c : codes) {
            if (code == c) return this;
        }

        // Construct StructuredException
        String type = meta.path("errorType").asText(null);
        String message = meta.path("errorMessage").asText(null);
        String detailed = meta.path("errorDetailed").asText(null);
        throw new StructuredException(code, type, message, detailed);
    }

    /**
     * Handle response for anything
     *
     * @param handler response handler
     * @return RestfulResponse
     */
    public RestfulResponse handle(Consumer<RestfulResponse> handler) {
        handler.accept(this);
        return this;
    }

    /**
     * @param clazz class of object
     * @param <T>   type of object
     * @return object if data node is present
     * return null if node is null or missing
     */
    public <T> T asDataObject(Class<T> clazz) {
        try {
            JsonNode data = getDataNode();
            if (data.isNull() || data.isMissingNode()) return null;
            return mapper.treeToValue(data, clazz);
        } catch (JsonProcessingException e) {
            throw ExceptionParser.handle(e);
        }
    }

    public <T> List<T> asDataList(Class<T> clazz) {
        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return mapper.convertValue(getDataNode(), type);
    }
}