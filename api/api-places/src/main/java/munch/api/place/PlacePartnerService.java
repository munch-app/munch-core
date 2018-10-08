package munch.api.place;

import munch.api.ApiService;
import munch.article.data.Article;
import munch.article.link.ArticleLinkClient;
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
    private final ArticleLinkClient articleLinkClient;

    @Inject
    public PlacePartnerService(InstagramLinkClient instagramLinkClient, ArticleLinkClient articleLinkClient) {
        this.instagramLinkClient = instagramLinkClient;
        this.articleLinkClient = articleLinkClient;
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
        String nextSort = call.queryString("next.sort", null);

        return articleLinkClient.list(placeId, nextSort, size);
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
