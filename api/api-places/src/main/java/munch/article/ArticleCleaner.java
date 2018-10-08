package munch.article;

import munch.api.ObjectCleaner;
import munch.article.data.Article;

/**
 * Created by: Fuxing
 * Date: 5/10/18
 * Time: 10:59 PM
 * Project: munch-core
 */
public final class ArticleCleaner extends ObjectCleaner<Article> {
    @Override
    protected Class<Article> getClazz() {
        return Article.class;
    }

    @Override
    protected void clean(Article data) {
        data.setCount(null);
    }
}
