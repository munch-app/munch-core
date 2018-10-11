package munch.api.feed;

import munch.api.ApiService;
import munch.article.data.Article;
import munch.article.data.ArticleClient;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import org.apache.commons.lang3.RandomUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by: Fuxing
 * Date: 11/10/18
 * Time: 2:15 PM
 * Project: munch-core
 */
@Singleton
public final class FeedService extends ApiService {
    private static final int SIZE = 30;
    private final ArticleClient articleClient;

    private static final List<String> NEXT_IDS;

    static {
        NEXT_IDS = new ArrayList<>();
        int slice = 32;
        long part = Long.MAX_VALUE / 32 * 2;
        for (int i = 0; i < slice; i++) {
            NEXT_IDS.add(new UUID(part * i, 0).toString());
        }
    }

    public static void main(String[] args) {
        System.out.println(NEXT_IDS);
    }

    @Inject
    public FeedService(ArticleClient articleClient) {
        this.articleClient = articleClient;
    }

    @Override
    public void route() {
        PATH("/feed", () -> {
            GET("", this::get);
        });
    }

    public JsonResult get(JsonCall call) {
        String nextSort = call.queryString("next.sort", null);
        if (nextSort != null) {
            return asResult(null);
        } else {
            String next = NEXT_IDS.get(RandomUtils.nextInt(0, NEXT_IDS.size()));
            return asResult(next);
        }
    }

    private JsonResult asResult(String next) {
        NextNodeList<Article> articles = articleClient.list(next, SIZE);
        next = articles.getNextString("articleId", null);

        if (next == null) return JsonResult.ok(articles);

        return JsonResult.ok(articles)
                .put("next", Map.of(
                        "sort", next
                ));
    }

    // 00000000-0000-0000-0000-000000000000
}
