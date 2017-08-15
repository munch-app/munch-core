package munch.api.clients;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.data.Article;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 7/6/2017
 * Time: 12:37 PM
 * Project: munch-core
 */
@Singleton
public final class ArticleClient extends RestfulClient {

    @Inject
    public ArticleClient(Config config) {
        super(config.getString("services.articles.url"));
    }

    /**
     * Query Articles that belong to place id
     *
     * @param placeId place id
     * @param from    from
     * @param size    size
     * @return List of Article
     */
    public List<Article> list(String placeId, int from, int size) {
        return doGet("/places/{placeId}/articles/list")
                .path("placeId", placeId)
                .queryString("from", from)
                .queryString("size", size)
                .asDataList(Article.class);
    }
}
