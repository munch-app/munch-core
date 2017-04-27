package munch.restful.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
import munch.restful.client.exception.ExceptionParser;
import munch.restful.client.exception.StructuredException;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 4:13 PM
 * Project: munch-core
 */
public class RestfulRequest {
    protected static final ObjectMapper mapper = RestfulClient.mapper;

    protected final HttpRequestWithBody request;
    protected MultipartBody multipartBody;

    public RestfulRequest(HttpMethod method, String url) {
        this.request = new HttpRequestWithBody(method, url);
    }

    public RestfulRequest basicAuth(String username, String password) {
        request.basicAuth(username, password);
        return this;
    }

    /**
     * @param name  name in path put {id}
     * @param value value for replace in place
     * @return this
     */
    public RestfulRequest path(String name, String value) {
        request.routeParam(name, value);
        return this;
    }

    /**
     * @param name  name in path put {id}
     * @param value value for replace in place
     * @return this
     */
    public RestfulRequest path(String name, int value) {
        request.routeParam(name, Integer.toString(value));
        return this;
    }

    /**
     * @param name  name in path put {id}
     * @param value value for replace in place
     * @return this
     */
    public RestfulRequest path(String name, long value) {
        request.routeParam(name, Long.toString(value));
        return this;
    }

    /**
     * @param name  name in path put {id}
     * @param value value for replace in place
     * @return this
     */
    public RestfulRequest path(String name, Object value) {
        request.routeParam(name, value.toString());
        return this;
    }

    public RestfulRequest header(String name, String value) {
        request.header(name, value);
        return this;
    }

    public RestfulRequest headers(Map<String, String> headers) {
        request.headers(headers);
        return this;
    }

    public RestfulRequest queryString(Map<String, Object> parameters) {
        request.queryString(parameters);
        return this;
    }

    public RestfulRequest queryString(String name, Object value) {
        request.queryString(name, value);
        return this;
    }

    public RestfulRequest field(String name, Collection<?> value) {
        if (multipartBody == null) multipartBody = request.field(name, value);
        else multipartBody.field(name, value);
        return this;
    }

    public RestfulRequest field(String name, Object value) {
        if (multipartBody == null) multipartBody = request.field(name, value);
        else multipartBody.field(name, value);
        return this;
    }

    public RestfulRequest field(String name, File file) {
        if (multipartBody == null) multipartBody = request.field(name, file);
        else multipartBody.field(name, file);
        return this;
    }

    public RestfulRequest field(String name, File file, String contentType) {
        if (multipartBody == null) multipartBody = request.field(name, file, contentType);
        else multipartBody.field(name, file, contentType);
        return this;
    }

    public RestfulRequest fields(Map<String, Object> parameters) {
        if (multipartBody == null) multipartBody = request.fields(parameters);
        return this;
    }

    public RestfulRequest field(String name, InputStream stream, ContentType contentType, String fileName) {
        if (multipartBody == null) multipartBody = request.field(name, stream, contentType, fileName);
        else multipartBody.field(name, stream, contentType, fileName);
        return this;
    }

    public RestfulRequest field(String name, InputStream stream, String fileName) {
        if (multipartBody == null) multipartBody = request.field(name, stream, fileName);
        else multipartBody.field(name, stream, fileName);
        return this;
    }

    /**
     * @param body raw bytes body
     * @return this
     */
    public RestfulRequest body(byte[] body) {
        request.body(body);
        return this;
    }

    /**
     * @param object object to convert to json
     * @return this
     */
    public RestfulRequest body(Object object) {
        try {
            request.body(mapper.writeValueAsBytes(object));
            return this;
        } catch (JsonProcessingException e) {
            throw ExceptionParser.handle(e);
        }
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

    /**
     * Check that meta json has follow codes
     * if not structured error will be thrown
     *
     * @param codes allowed codes in meta
     * @return RestfulResponse
     * @throws StructuredException if codes are not matched
     */
    public RestfulResponse hasMetaCodes(int... codes) throws StructuredException {
        return asResponse().hasMetaCodes(codes);
    }

    /**
     * Using default exception parser
     *
     * @return RestfulResponse
     */
    public RestfulResponse asResponse() {
        return asResponse((exception, response) -> {
            if (exception != null) throw ExceptionParser.handle(exception);
        });
    }

    public <E extends Exception> RestfulResponse asResponse(ResponseHandler<E> handler) throws E {
        try {
            RestfulResponse response = new RestfulResponse(request.asBinary());
            handler.handle(null, response);
            return response;
        } catch (UnirestException e) {
            handler.handle((Exception) e.getCause(), null);
            return null;
        }
    }
}
