package munch.restful.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import munch.restful.client.exception.ExceptionHandler;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 4:13 PM
 * Project: munch-core
 */
public class RestfulRequest extends HttpRequestWithBody {

    public RestfulRequest(HttpMethod method, String url) {
        super(method, url);
    }

    public JsonNode asNode() {
        return asResponse().getNode();
    }

    public JsonNode asDataNode() {
        return asResponse().getDataNode();
    }

    public <T> T asDataObject(Class<T> clazz) {
        return asResponse().asDataObject(clazz);
    }

    public <T> List<T> asDataList(Class<T> clazz) {
        return asResponse().asDataList(clazz);
    }

    public RestfulResponse asResponse() {
        try {
            return new RestfulResponse(asBinary());
        } catch (UnirestException e) {
            throw ExceptionHandler.handle(e);
        }
    }
}
