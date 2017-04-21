package munch.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.endpoints.service.Article;
import munch.api.endpoints.service.Graphic;
import munch.api.endpoints.service.PlaceClient;
import munch.api.struct.Place;
import munch.restful.server.JsonCall;

import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceEndpoint extends AbstractEndpoint {

    private final PlaceClient placeClient;

    @Inject
    public PlaceEndpoint(PlaceClient placeClient) {
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);

            GET("/:placeId", this::get);

            GET("/:placeId/gallery", this::gallery);
            GET("/:placeId/articles", this::articles);
        });
    }

    /**
     * @param call    json call
     * @param request body node
     * @return list of place result
     */
    private List<Place> search(JsonCall call, JsonNode request) {
        int from = request.path("from").asInt();
        int size = request.path("size").asInt();

        // Check geometry node exist
        JsonNode geometry = null;
        if (request.has("geometry")) {
            geometry = request.path("geometry");
        }
        return placeClient.search(from, size, geometry);
    }

    private Place get(JsonCall call) {
        String placeId = call.pathString("placeId");
        return placeClient.get(placeId);
    }

    private List<Graphic> gallery(JsonCall call) {
        String placeId = call.pathString("placeId");
        return Collections.emptyList();
    }

    private List<Article> articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        return Collections.emptyList();
    }
}
