package munch.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.places.data.MenuDatabase;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 9:09 PM
 * Project: munch-core
 */
@Singleton
public class UploadService implements JsonService {

    private final MenuDatabase database;

    @Inject
    public UploadService(MenuDatabase database) {
        this.database = database;
    }

    @Override
    public void route() {
        PATH("/places/:placeId/menu", () -> {
            PUT("/:menuId/image", this::putImage);
            PUT("/:menuId/pdf", this::putPdf);
            PUT("/:menuId/website", this::putWebsite);

            DELETE("/:menuId", this::delete);
        });
    }

    private JsonNode putImage(JsonCall call, JsonNode request) {
        String menuId = call.pathString("menuId");
        // TODO put image
        return Meta200;
    }

    private JsonNode putPdf(JsonCall call, JsonNode request) {
        String menuId = call.pathString("menuId");
        // TODO put pdf
        return Meta200;
    }

    private JsonNode putWebsite(JsonCall call, JsonNode request) {
        String menuId = call.pathString("menuId");
        String website = request.get("website").asText();
        // TODO put website
        return Meta200;
    }

    private JsonNode delete(JsonCall call) {
        String menuId = call.pathString("menuId");
        database.delete(menuId);
        return Meta200;
    }
}
