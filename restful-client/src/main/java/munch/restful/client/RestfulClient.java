package munch.restful.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpMethod;
import munch.restful.client.exception.ExceptionHandler;

import java.util.ArrayList;
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

    protected RestfulRequest get(String path) {
        return new RestfulRequest(HttpMethod.GET, path(path));
    }

    protected RestfulRequest head(String path) {
        return new RestfulRequest(HttpMethod.HEAD, path(path));
    }

    protected RestfulRequest options(String path) {
        return new RestfulRequest(HttpMethod.OPTIONS, path(path));
    }

    protected RestfulRequest post(String path) {
        return new RestfulRequest(HttpMethod.POST, path(path));
    }

    protected RestfulRequest delete(String path) {
        return new RestfulRequest(HttpMethod.DELETE, path(path));
    }

    protected RestfulRequest patch(String path) {
        return new RestfulRequest(HttpMethod.PATCH, path(path));
    }

    protected RestfulRequest put(String path) {
        return new RestfulRequest(HttpMethod.PUT, path(path));
    }

    public <T> T toObject(JsonNode node, Class<T> clazz) {
        try {
            return mapper.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            throw ExceptionHandler.handle(e);
        }
    }

    public <T> List<T> toList(JsonNode nodes, Class<T> clazz) {
        try {
            List<T> list = new ArrayList<>();
            for (JsonNode node : nodes) {
                list.add(mapper.treeToValue(node, clazz));
            }
            return list;
        } catch (JsonProcessingException e) {
            throw ExceptionHandler.handle(e);
        }
    }
}
