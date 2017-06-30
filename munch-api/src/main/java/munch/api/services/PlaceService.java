package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.ArticleClient;
import munch.api.clients.MediaClient;
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
    private final MediaClient mediaClient;

    private final CollectionMaker categorizer;

    @Inject
    public PlaceService(PlaceClient placeClient, ArticleClient articleClient, MediaClient mediaClient, CollectionMaker categorizer) {
        this.placeClient = placeClient;
        this.articleClient = articleClient;
        this.mediaClient = mediaClient;
        this.categorizer = categorizer;
    }

    /**
     * Endpoint: /v/places/*
     * Endpoint: /v/places/:placeId/*
     */
    @Override
    public void route() {
        PATH("/places", () -> {
            POST("/suggest", this::suggest);

            POST("/search", this::search);
            POST("/search/scroll", this::searchScroll);

            // Single place endpoint
            PATH("/:placeId", () -> {
                GET("", this::get);

                GET("/medias/list", this::medias);
                GET("/articles/list", this::articles);
            });
        });
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
     * @return list of Place result
     */
    private List<PlaceCollection> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return categorizer.search(query, getHeaderLatLng(call).orElse(null));
    }

    /**
     * @param call json call
     * @return list of Place
     * @see SearchQuery
     */
    private List<Place> searchScroll(JsonCall call) {
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
        detail.setMedias(mediaClient.list(placeId, 0, 10));
        detail.setArticles(articleClient.list(placeId, 0, 10));
        return detail;
    }

    private List<Media> medias(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return mediaClient.list(placeId, from, size);
    }

    private List<Article> articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return articleClient.list(placeId, from, size);
    }
}
