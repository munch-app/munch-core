package munch.api.services.places.loader;

import munch.article.clients.Article;
import munch.article.clients.ArticleClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        super("extended_PartnerArticle_20180506");
        this.client = client;
    }

    @Override
    protected List<Article> query(String placeId) {
        List<Article> articleList = client.list(placeId, null, 10);
        removeBadData(articleList);
        return articleList;
    }

    public static void removeBadData(List<Article> articles) {
        Set<String> uniquePairs = new HashSet<>();

        articles.removeIf(article -> {
            // No description article will be removed for now
            if (article.getDescription() == null) return true;

            String pair = article.getBrand() + "|" + article.getTitle();
            if (uniquePairs.contains(pair)) return true;

            uniquePairs.add(pair);
            return false;
        });
    }
}
