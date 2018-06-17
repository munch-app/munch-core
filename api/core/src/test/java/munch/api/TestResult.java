package munch.api;

import munch.restful.client.RestfulResponse;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 18/6/18
 * Time: 2:27 AM
 * Project: munch-core
 */
public final class TestResult {
    public final int code;
    public final Map<String, Object> params;
    public final Consumer<RestfulResponse> consumer;

    public TestResult(int code, Map<String, Object> params, Consumer<RestfulResponse> consumer) {
        this.code = code;
        this.params = params;
        this.consumer = consumer;
    }

    public static TestResult of(String name, Object value) {
        return of(200, Map.of(name, value), n -> {
        });
    }

    public static TestResult of(int code, String name, Object value) {
        return of(code, Map.of(name, value), n -> {
        });
    }

    public static TestResult of(Map<String, Object> params) {
        return of(params, n -> {
        });
    }

    public static TestResult of(Map<String, Object> params, Consumer<RestfulResponse> consumer) {
        return of(200, params, consumer);
    }

    public static TestResult of(int code, Map<String, Object> params, Consumer<RestfulResponse> consumer) {
        return new TestResult(code, params, consumer);
    }

    public void validate(RestfulResponse response) {
        response.hasCode(code);
        consumer.accept(response);
    }

    @Override
    public String toString() {
        return "TestResult(" +
                +code +
                ", " + params +
                ')';
    }
}
