package munch.restful.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.mashape.unirest.http.HttpMethod;
import munch.restful.client.exception.ExceptionParser;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
public abstract class RestfulClient {
    protected static final ObjectMapper mapper = new ObjectMapper();

    private final String url;

    /**
     * @param url must not end with /
     */
    public RestfulClient(String url) {
        this.url = url;
    }

    /**
     * @param path path must begin with /
     * @return full path
     */
    private String path(String path) {
        return url + path;
    }

    protected RestfulRequest doGet(String path) {
        return new RestfulRequest(HttpMethod.GET, path(path));
    }

    protected RestfulRequest doHead(String path) {
        return new RestfulRequest(HttpMethod.HEAD, path(path));
    }

    protected RestfulRequest doOptions(String path) {
        return new RestfulRequest(HttpMethod.OPTIONS, path(path));
    }

    protected RestfulRequest doPost(String path) {
        return new RestfulRequest(HttpMethod.POST, path(path));
    }

    protected RestfulRequest doDelete(String path) {
        return new RestfulRequest(HttpMethod.DELETE, path(path));
    }

    protected RestfulRequest doPatch(String path) {
        return new RestfulRequest(HttpMethod.PATCH, path(path));
    }

    protected RestfulRequest doPut(String path) {
        return new RestfulRequest(HttpMethod.PUT, path(path));
    }

    public static <T> T toObject(JsonNode node, Class<T> clazz) {
        try {
            return mapper.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw ExceptionParser.handle(e);
        }
    }

    public static <T> List<T> toList(JsonNode nodes, Class<T> clazz) {
        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return mapper.convertValue(nodes, type);
    }
}
