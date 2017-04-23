package munch.api.services.places;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.PlaceClient;
import munch.api.services.AbstractService;
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
public class PlaceService extends AbstractService {

    private final PlaceClient placeClient;

    @Inject
    public PlaceService(PlaceClient placeClient) {
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/search", this::search);

            PATH("/:placeId", () -> {
                GET("", this::get);

                GET("/gallery", this::gallery);
                GET("/articles", this::articles);
                GET("/reviews", this::reviews);
            });
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

    private List<String> reviews(JsonCall call) {
        String placeId = call.pathString("placeId");
        return Collections.emptyList();
    }
}
