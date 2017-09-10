package munch.api.services.places;

import munch.api.clients.DataClient;
import munch.data.Article;
import munch.data.Place;
import munch.data.places.ArticleGridCard;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/9/2017
 * Time: 1:33 PM
 * Project: munch-core
 */
@Singleton
public final class VendorCardGenerator {

    private final DataClient dataClient;

    @Inject
    public VendorCardGenerator(DataClient dataClient) {
        this.dataClient = dataClient;
    }

    @Nullable
    ArticleGridCard createArticleGrid(Place place) {
        List<Article> articles = dataClient.getArticles(place.getId(), null, 4);
        if (articles.isEmpty()) return null;

        ArticleGridCard card = new ArticleGridCard();
        card.setArticles(articles);
        return card;
    }
}