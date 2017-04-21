package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.ImageLink;
import munch.places.data.ImageLinkDatabase;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 6:13 PM
 * Project: munch-core
 */
@Singleton
public class ImageLinkService implements JsonService {

    private final ImageLinkDatabase database;

    @Inject
    public ImageLinkService(ImageLinkDatabase database) {
        this.database = database;
    }

    /**
     * For place image link service, you don't actually store any image here
     * physically, you link image from other content
     * Image is actually stored in munch-service-images
     */
    @Override
    public void route() {
        PATH("/places/:placeId", () -> {
            GET("", this::get);

            PATH("/:imageKey", () -> {
                PUT("", this::put);
                DELETE("", this::delete);
            });
        });
    }

    private List<ImageLink> get(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return database.query(placeId, from, size);
    }

    private JsonNode put(JsonCall call, JsonNode request) {
        String placeId = call.pathString("placeId");
        String imageKey = call.pathString("imageKey");
        String sourceName = request.get("sourceName").asText();
        String sourceId = request.get("sourceId").asText();
        database.put(placeId, imageKey, sourceName, sourceId);
        return Meta200;
    }

    private JsonNode delete(JsonCall call) {
        // String placeId = call.pathString("placeId");
        String imageKey = call.pathString("imageKey");
        database.delete(Collections.singletonList(imageKey));
        return Meta200;
    }
}
