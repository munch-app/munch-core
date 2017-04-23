package munch.api.services.places;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.ImageClient;
import munch.api.clients.PlaceClient;
import munch.api.services.AbstractService;
import munch.api.struct.Article;
import munch.api.struct.Graphic;
import munch.api.struct.Place;
import munch.api.struct.PlaceCollection;
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
    private final ImageClient imageClient;

    private final PlaceGrouping placeGrouping;

    @Inject
    public PlaceService(PlaceClient placeClient, ImageClient imageClient, PlaceGrouping placeGrouping) {
        this.placeClient = placeClient;
        this.imageClient = imageClient;
        this.placeGrouping = placeGrouping;
    }

    /**
     * Endpoint: /v/places/*
     * Endpoint: /v/places/:placeId/*
     */
    @Override
    public void route() {
        PATH("/places", () -> {
            GET("/suggest", this::suggest);
            POST("/search", this::search);

            // Single place endpoint
            PATH("/:placeId", () -> {
                GET("", this::get);

                GET("/gallery", this::gallery);
                GET("/articles", this::articles);
                GET("/reviews", this::reviews);
            });
        });
    }

    /**
     * ?text={String}&size={Int}
     *
     * @param call json call
     * @return list of Place result
     */
    private List<Place> suggest(JsonCall call) {
        int size = call.queryInt("size");
        String text = call.queryString("text");
        return placeClient.suggest(size, text);
    }

    /**
     * @param call json call
     * @return list of Place result
     */
    private List<PlaceCollection> search(JsonCall call) {
        ObjectNode node = call.bodyAsJson().deepCopy();
        node.put("size", 40);
        List<Place> places = placeClient.search(node);
        return placeGrouping.parse(places, 1, 1, 1);
    }

    /**
     * GET = /:placeId
     *
     * @param call json call
     * @return Place or Null
     */
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
