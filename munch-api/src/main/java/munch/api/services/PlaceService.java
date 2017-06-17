package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.ArticleClient;
import munch.api.clients.GalleryClient;
import munch.api.clients.PlaceClient;
import munch.api.data.*;
import munch.restful.server.JsonCall;

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
    private final ArticleClient articleClient;
    private final GalleryClient galleryClient;

    private final PlaceCategorizer categorizer;

    @Inject
    public PlaceService(PlaceClient placeClient, ArticleClient articleClient, GalleryClient galleryClient, PlaceCategorizer categorizer) {
        this.placeClient = placeClient;
        this.articleClient = articleClient;
        this.galleryClient = galleryClient;
        this.categorizer = categorizer;
    }

    /**
     * Endpoint: /v/places/*
     * Endpoint: /v/places/:placeId/*
     */
    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/categorize", this::categorize);
            POST("/suggest", this::suggest);
            POST("/search", this::search);

            // Single place endpoint
            PATH("/:placeId", () -> {
                GET("", this::get);

                GET("/gallery/list", this::gallery);
                GET("/articles/list", this::articles);
            });
        });
    }

    /**
     * @param call json call
     * @return list of Place result
     */
    private List<PlaceCollection> categorize(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        query.setSize(40);

        List<Place> places = placeClient.search(query);
        return categorizer.categorize(query, places);
    }

    /**
     * <pre>
     *  {
     *      size: 10,
     *      text: "",
     *      latLng: "" // Optional
     *  }
     * </pre>
     *
     * @param call json call
     * @return list of Place result
     */
    private List<Place> suggest(JsonCall call, JsonNode request) {
        int size = request.get("size").asInt();
        String text = request.get("text").asText();
        String latLng = request.path("latLng").asText(null); // Nullable
        return placeClient.suggest(size, text, latLng);
    }

    /**
     * @param call json call
     * @return list of Place
     * @see SearchQuery
     */
    private List<Place> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return placeClient.search(query);
    }

    /**
     * GET = /:placeId
     *
     * @param call json call
     * @return Place or Null
     */
    private PlaceDetail get(JsonCall call) {
        String placeId = call.pathString("placeId");
        Place place = placeClient.get(placeId);
        if (place == null) return null;

        // PlaceDetail: query 10 articles and medias
        PlaceDetail detail = new PlaceDetail();
        detail.setPlace(place);
        detail.setMedias(galleryClient.list(placeId, 0, 10));
        detail.setArticles(articleClient.list(placeId, 0, 10));
        return detail;
    }

    private List<Media> gallery(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return galleryClient.list(placeId, from, size);
    }

    private List<Article> articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return articleClient.list(placeId, from, size);
    }
}
