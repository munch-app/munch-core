package munch.api.place.query;

import munch.article.ArticleLinkClient;
import munch.article.data.Article;

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

    private final ArticleLinkClient articleLinkClient;

    @Inject
    public PlaceArticleCardLoader(ArticleLinkClient articleLinkClient) {
        super("extended_PartnerArticle_20180506");
        this.articleLinkClient = articleLinkClient;
    }

    @Override
    protected List<Article> query(String placeId) {
        return articleLinkClient.list(placeId, null, 10);
    }
}
