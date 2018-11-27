package munch.api.feed;

import munch.data.client.PlaceCachedClient;
import munch.feed.ArticleFeedClient;
import munch.feed.ArticleFeedItem;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 27/11/18
 * Time: 9:44 AM
 * Project: munch-core
 */
@Singleton
public final class FeedArticleService extends FeedService {

    private final ArticleFeedClient articleFeedClient;

    @Inject
    public FeedArticleService(PlaceCachedClient placeClient, ArticleFeedClient articleFeedClient) {
        super(placeClient);
        this.articleFeedClient = articleFeedClient;
    }

    @Override
    public void route() {
        PATH("/feed/articles", () -> {
            GET("", this::queryArticles);
            GET("/:itemId", this::getArticle);
        });
    }

    public JsonResult queryArticles(JsonCall call) {
        String country = call.queryString("country", "sgp");
        String latLng = call.queryString("latLng", "1.3521,103.8198");

        String nextSort = call.queryString("next.sort", null);
        int size = call.querySize(20, 30);

        NextNodeList<ArticleFeedItem> items = articleFeedClient.query(country, latLng, nextSort, size);
        return result(items);
    }

    private JsonResult getArticle(JsonCall call) {
        String itemId = call.pathString("itemId");
        ArticleFeedItem feedItem = articleFeedClient.get(itemId);
        if (feedItem == null) return JsonResult.notFound();
        return asResult(feedItem);
    }
}
