package munch.api.services.places.loader;

import munch.article.clients.Article;
import munch.article.clients.ArticleClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 27/4/2018
 * Time: 7:42 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceArticleCardLoader extends PlaceDataCardLoader<Article> {

    private final ArticleClient client;

    @Inject
    public PlaceArticleCardLoader(ArticleClient client) {
        super("extended_PartnerArticle_20180427");
        this.client = client;
    }

    @Override
    protected List<Article> query(String placeId) {
        return client.list(placeId, null, null, 10);
    }
}
