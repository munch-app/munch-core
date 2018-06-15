package munch.api.place;

import munch.api.ApiService;
import munch.api.place.query.PlaceArticleCardLoader;
import munch.article.clients.Article;
import munch.article.clients.ArticleClient;
import munch.corpus.instagram.InstagramMedia;
import munch.corpus.instagram.InstagramMediaClient;
import munch.restful.client.dynamodb.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

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
    private final ArticleClient articleClient;
    private final InstagramMediaClient instagramMediaClient;

    @Inject
    public PlacePartnerService(ArticleClient articleClient, InstagramMediaClient instagramMediaClient) {
        this.articleClient = articleClient;
        this.instagramMediaClient = instagramMediaClient;
    }

    @Override
    public void route() {
        PATH("/places/:placeId/partners", () -> {
            GET("/articles", this::getArticles);
            GET("/instagram/medias", this::getInstagramMedias);
        });
    }

    private JsonResult getArticles(JsonCall call) {
        int size = call.querySize(20, 40);
        String placeId = call.pathString("placeId");
        String nextPlaceSort = call.queryString("next.placeSort", null);

        NextNodeList<Article> nextNodeList = articleClient.list(placeId, nextPlaceSort, size);
        PlaceArticleCardLoader.removeBadData(nextNodeList);

        JsonResult result = result(200, nextNodeList);
        if (nextNodeList.hasNext()) result.put("next", nextNodeList.getNext());
        return result;
    }

    private JsonResult getInstagramMedias(JsonCall call) {
        int size = call.querySize(20, 40);
        String placeId = call.pathString("placeId");
        String nextPlaceSort = call.queryString("next.placeSort", null);

        NextNodeList<InstagramMedia> nextNodeList = instagramMediaClient.listByPlace(placeId, nextPlaceSort, size);

        JsonResult result = result(200, nextNodeList);
        if (nextNodeList.hasNext()) result.put("next", nextNodeList.getNext());
        return result;
    }
}
