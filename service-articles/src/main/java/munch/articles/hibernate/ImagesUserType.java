package munch.articles.hibernate;


import munch.articles.Article;

/**
 * Created By: Fuxing Loh
 * Date: 10/3/2017
 * Time: 2:30 PM
 * Project: munch-core
 */
public class ImagesUserType extends PojoUserType<Article.Image[]> {

    public ImagesUserType() {
        super(Article.Image[].class);
    }

}
