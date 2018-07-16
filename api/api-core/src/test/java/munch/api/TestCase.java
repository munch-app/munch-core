package munch.api;

import munch.restful.client.RestfulRequest;
import munch.restful.client.RestfulResponse;

import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 2:27 AM
 * Project: munch-core
 */
public final class TestCase {
    public final int code;
    private final String name;
    private Consumer<RestfulRequest> requestConsumer;
    private Consumer<RestfulResponse> responseConsumer;

    public TestCase(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TestCase of(int code, String name) {
        return new TestCase(code, name);
    }

    public static TestCase of(int code) {
        return new TestCase(code, null);
    }

    public TestCase request(Consumer<RestfulRequest> requestConsumer) {
        this.requestConsumer = requestConsumer;
        return this;
    }

    public TestCase response(Consumer<RestfulResponse> responseConsumer) {
        this.responseConsumer = responseConsumer;
        return this;
    }

    public RestfulResponse asResponse(RestfulRequest request) {
        if (requestConsumer != null) requestConsumer.accept(request);
        RestfulResponse response = request.asResponse();
        response.hasCode(code);
        if (responseConsumer != null) responseConsumer.accept(response);
        return response;
    }

    public boolean isOk() {
        return code == 200;
    }

    @Override
    public String toString() {
        if (name == null) return "TestCase(" + code + ')';
        return "TestCase(" + code + ", " + name + ')';
    }
}
