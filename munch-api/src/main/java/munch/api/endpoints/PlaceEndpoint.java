package munch.api.endpoints;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.PlaceClient;
import munch.api.struct.Article;
import munch.api.struct.Graphic;
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
     * @return list of place result
     */
    private List<Place> search(JsonCall call) {
        ObjectNode node = call.bodyAsJson().deepCopy();
        node.put("size", 40);
        return placeClient.search(node);
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
