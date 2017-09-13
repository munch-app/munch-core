package munch.data.places;

import munch.data.Article;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 10/9/2017
 * Time: 2:09 AM
 * Project: munch-core
 */
public final class PlaceArticleGridCard extends PlaceCard {

    private List<Article> articles;

    @Override
    public String getId() {
        return "vendor_ArticleGrid_10092017";
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
