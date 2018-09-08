package munch.api.place.query;

import munch.api.place.CatalystV2Support;
import munch.article.clients.Article;

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

    private final CatalystV2Support v2Support;

    @Inject
    public PlaceArticleCardLoader(CatalystV2Support v2Support) {
        super("extended_PartnerArticle_20180506");
        this.v2Support = v2Support;
    }

    @Override
    protected List<Article> query(String placeId) {
        List<Article> articleList = v2Support.getArticles(placeId, null, 10);
        removeBadData(articleList);
        return articleList;
    }

    public static void removeBadData(List<Article> articles) {
        Set<String> uniquePairs = new HashSet<>();
        Set<String> uniqueUrls = new HashSet<>();

        articles.removeIf(article -> {
            // No description article will be removed for now
            if (article.getDescription() == null) return true;
            if (uniqueUrls.contains(article.getUrl())) return true;

            String pair = article.getBrand() + "|" + article.getTitle();
            if (uniquePairs.contains(pair)) return true;

            uniquePairs.add(pair);
            uniqueUrls.add(article.getUrl());
            return false;
        });
    }
}
