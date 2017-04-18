package munch.images;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.images.database.Image;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 10:43 PM
 * Project: munch-core
 */
@Singleton
public class ImageService implements JsonService {

    // TODO image service

    @Override
    public void route() {
        PATH("/images", () -> {
            GET("/:key", this::get);
            PUT("/:key", this::put);
            DELETE("/:key", this::delete);

            new Batch().route();
        });
    }

    public Image get(JsonCall call) {
        String key = call.pathString("key");
        String[] types = mapTypes(call);

        return null;
    }

    public JsonNode put(JsonCall call) {
        String key = call.pathString("key");
        String[] types = mapTypes(call);

        return null;
    }

    public JsonNode delete(JsonCall call) {
        String key = call.pathString("key");

        return null;
    }

    private class Batch implements JsonService {

        @Override
        public void route() {
            PATH("/batch", () -> {
                POST("/get", this::get);
            });
        }

        public List<Image> get(JsonCall call) {
            List<String> keys = call.bodyAsList(String.class);
            String[] types = mapTypes(call);

            return null;
        }
    }

    /**
     * @param call json call
     * @return types in String array
     */
    @Nullable
    public static String[] mapTypes(JsonCall call) {
        return Optional.of(call.request().queryParams("types"))
                .map(s -> s.split(","))
                .orElse(null);
    }
}
