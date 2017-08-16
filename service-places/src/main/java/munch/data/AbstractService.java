package munch.data;

import com.fasterxml.jackson.databind.JsonNode;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 16/8/2017
 * Time: 9:23 PM
 * Project: munch-core
 */
public abstract class AbstractService<T> implements JsonService {
    protected final String type;
    protected final Class<T> clazz;

    protected AbstractService(String type, Class<T> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    @Override
    public void route() {
        PATH("/" + type, () -> {
            POST("/get", this::batchGet);

            GET("/:id", this::get);
            PUT("/:id", this::put);
            DELETE("/:id", this::delete);
        });
    }

    protected List<T> batchGet(JsonCall call) {
        return null;
    }

    protected T get(JsonCall call) {
        return null;
    }

    protected JsonNode put(JsonCall call) {
        return Meta200;
    }

    protected JsonNode delete(JsonCall call) {
        return Meta200;
    }
}
