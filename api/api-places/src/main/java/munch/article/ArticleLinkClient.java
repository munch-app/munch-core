package munch.article;

import catalyst.link.PlaceLinkClient;
import munch.article.data.Article;
import munch.article.data.ArticleClient;
import munch.article.data.DomainCache;
import munch.restful.core.NextNodeList;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 8/10/18
 * Time: 6:47 PM
 * Project: munch-core
 */
@Singleton
public final class ArticleLinkClient extends munch.article.link.ArticleLinkClient {

    @Inject
    public ArticleLinkClient(PlaceLinkClient placeLinkClient, ArticleClient articleClient, DomainCache domainCache) {
        super(placeLinkClient, articleClient, domainCache);
    }

    @Override
    public NextNodeList<Article> list(String placeId, @Nullable String nextSort, int size) {
        NextNodeList<Article> list = super.list(placeId, nextSort, size);
        sanitize(list);
        return list;
    }

    private static void sanitize(List<Article> articles) {
        Set<String> uniquePairs = new HashSet<>();
        Set<String> uniqueUrls = new HashSet<>();

        articles.removeIf(article -> {
            // No description article will be removed for now
            if (article.getDescription() == null && article.getContent() == null) return true;
            if (uniqueUrls.contains(article.getUrl())) return true;

            String pair = article.getDomainId() + "|" + article.getTitle();
            if (uniquePairs.contains(pair)) return true;

            uniquePairs.add(pair);
            uniqueUrls.add(article.getUrl());
            return false;
        });
    }
}