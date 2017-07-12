package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.ArticleClient;
import munch.api.clients.InstagramClient;
import munch.api.clients.PlaceClient;
import munch.api.data.Article;
import munch.api.data.InstagramMedia;
import munch.api.data.Place;
import munch.api.data.PlaceDetail;
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
    private final InstagramClient instagramClient;

    @Inject
    public PlaceService(PlaceClient placeClient, ArticleClient articleClient, InstagramClient instagramClient) {
        this.placeClient = placeClient;
        this.articleClient = articleClient;
        this.instagramClient = instagramClient;
    }

    /**
     * Endpoint: /v/places/*
     * Endpoint: /v/places/:placeId/*
     */
    @Override
    public void route() {
        // Single place endpoint
        PATH("/places/:placeId", () -> {
            GET("", this::get);

            GET("/instagram/medias/list", this::medias);
            GET("/articles/list", this::articles);
        });
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
        detail.setInstagram(new PlaceDetail.Instagram());
        detail.getInstagram().setMedias(instagramClient.list(placeId, 0, 20));
        detail.setArticles(articleClient.list(placeId, 0, 10));
        return detail;
    }

    private List<InstagramMedia> medias(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return instagramClient.list(placeId, from, size);
    }

    private List<Article> articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return articleClient.list(placeId, from, size);
    }
}
