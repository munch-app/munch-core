package munch.api.place;

import munch.api.ApiService;
import munch.api.place.query.PlaceArticleCardLoader;
import munch.article.clients.Article;
import munch.instagram.InstagramLinkClient;
import munch.instagram.data.InstagramMedia;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 6:45 PM
 * Project: munch-core
 */
@Singleton
public final class PlacePartnerService extends ApiService {
    private final InstagramLinkClient instagramLinkClient;

    private final CatalystV2Support v2Support;

    @Inject
    public PlacePartnerService(InstagramLinkClient instagramLinkClient, CatalystV2Support v2Support) {
        this.instagramLinkClient = instagramLinkClient;
        this.v2Support = v2Support;
    }

    @Override
    public void route() {
        PATH("/places/:placeId/partners", () -> {
            GET("/articles", this::getArticles);
            GET("/instagram/medias", this::getInstagramMedias);
        });
    }

    private NextNodeList<Article> getArticles(JsonCall call) {
        int size = call.querySize(20, 40);
        String placeId = call.pathString("placeId");
        String nextPlaceSort = call.queryString("next.placeSort", null);

        NextNodeList<Article> nextNodeList = v2Support.getArticles(placeId, nextPlaceSort, size);
        PlaceArticleCardLoader.removeBadData(nextNodeList);
        return nextNodeList;
    }

    /**
     * if next = null no more data to query,
     * if next != null pass it into queryString ?next.sort=abc
     *
     * @return {'data': [...], 'next': {'sort': ''}, 'meta': {...}}
     */
    private NextNodeList<InstagramMedia> getInstagramMedias(JsonCall call) {
        int size = call.querySize(20, 40);
        String placeId = call.pathString("placeId");
        String nextSort = call.queryString("next.sort", null);
        return instagramLinkClient.list(placeId, nextSort, size);
    }
}
