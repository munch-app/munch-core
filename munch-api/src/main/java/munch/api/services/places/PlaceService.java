package munch.api.services.places;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
    private final PlaceExternalResolver placeResolver;

    private final PlaceGrouping placeGrouping;

    @Inject
    public PlaceService(PlaceClient placeClient, PlaceExternalResolver placeResolver, PlaceGrouping placeGrouping) {
        this.placeClient = placeClient;
        this.placeResolver = placeResolver;
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
        // TODO tag search
        ObjectNode node = call.bodyAsJson().deepCopy();
        node.put("size", 40);
        List<Place> places = placeClient.search(node);
        placeResolver.resolve(places, call);
        return placeGrouping.parse(places);
    }

    /**
     * GET = /:placeId
     *
     * @param call json call
     * @return Place or Null
     */
    private Place get(JsonCall call) {
        String placeId = call.pathString("placeId");
        Place place = placeClient.get(placeId);
        if (place == null) return null;

        placeResolver.resolve(place, call);
        return place;
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
