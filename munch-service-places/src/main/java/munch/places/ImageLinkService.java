package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.ImageLink;
import munch.places.data.ImageLinkDatabase;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

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
            GET("", this::list);

            PATH("/batch", () -> {
                PUT("", this::put);
                DELETE("", this::delete);
            });
        });
    }

    /**
     * @param call json call
     * @return List of ImageLink
     */
    private List<ImageLink> list(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return database.query(placeId, from, size);
    }

    /**
     * @param call    json call
     * @param request json data
     * @return 200 = ok, else error
     */
    private JsonNode put(JsonCall call, JsonNode request) {
        String placeId = call.pathString("placeId");
        for (JsonNode node : request) {
            String imageKey = node.get("imageKey").asText();
            String sourceName = node.get("sourceName").asText();
            String sourceId = node.get("sourceId").asText();
            database.put(placeId, imageKey, sourceName, sourceId);
        }
        return Meta200;
    }

    /**
     * @param call json call
     * @return 200 = ok, else error
     */
    private JsonNode delete(JsonCall call) {
        List<String> keys = call.bodyAsList(node -> node.get("imageKey").asText());
        database.delete(keys);
        return Meta200;
    }
}
