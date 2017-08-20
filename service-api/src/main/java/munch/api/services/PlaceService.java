package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.clients.DataClient;
import munch.data.Article;
import munch.data.InstagramMedia;
import munch.data.Place;
import munch.data.PlaceDetail;
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
    private final DataClient dataClient;

    @Inject
    public PlaceService(DataClient dataClient) {
        this.dataClient = dataClient;
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
        // Explicit card will be generate at mobile app
        // data: { place: {}, cards: [] }
        // Other card, are generate here
        // Card: name, version, data


        String placeId = call.pathString("placeId");
        Place place = dataClient.get(placeId);
        if (place == null) return null;

        // PlaceDetail: query 10 articles and medias
        PlaceDetail detail = new PlaceDetail();
        detail.setPlace(place);
        detail.setInstagram(new PlaceDetail.Instagram());
        detail.getInstagram().setMedias(dataClient.getInstagramMedias(placeId, 0, 20));
        detail.setArticles(dataClient.getArticles(placeId, 0, 10));
        return detail;
    }

    private List<InstagramMedia> medias(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return dataClient.getInstagramMedias(placeId, from, size);
    }

    private List<Article> articles(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return dataClient.getArticles(placeId, from, size);
    }
}
