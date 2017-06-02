package munch.gallery;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 7:00 PM
 * Project: munch-core
 */
@Singleton
public class GalleryService implements JsonService {

    @Override
    public void route() {
        PATH("/places/:placeId/gallery", () -> {
            GET("/list", this::list);
            GET("/:graphicId", this::get);

            // Management
            PUT("/:graphicId", this::put);
            DELETE("/:graphicId", this::delete);
            DELETE("/before/:timestamp", this::deleteBefore);
        });
    }

    private JsonNode list(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return null;
    }

    private JsonNode get(JsonCall call) {
        String placeId = call.pathString("placeId");
        String graphicId = call.pathString("graphicId");
        return null;
    }

    private JsonNode put(JsonCall call) {
        String placeId = call.pathString("placeId");
        String graphicId = call.pathString("graphicId");
        return null;
    }

    private JsonNode delete(JsonCall call) {
        String placeId = call.pathString("placeId");
        String graphicId = call.pathString("graphicId");
        return null;
    }

    private JsonNode deleteBefore(JsonCall call) {
        String placeId = call.pathString("placeId");
        Date before = new Date(call.pathLong("timestamp"));
        return null;
    }
}
