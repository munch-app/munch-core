package munch.api.place.query;

import munch.api.place.PlaceCatalystV2Support;
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
    private final PlaceCatalystV2Support v2Support;

    @Inject
    public PlaceArticleCardLoader(ArticleClient client, PlaceCatalystV2Support v2Support) {
        super("extended_PartnerArticle_20180506");
        this.client = client;
        this.v2Support = v2Support;
    }

    @Override
    protected List<Article> query(String placeId) {
        List<Article> articleList = client.list(v2Support.resolve(placeId), null, 10);
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
