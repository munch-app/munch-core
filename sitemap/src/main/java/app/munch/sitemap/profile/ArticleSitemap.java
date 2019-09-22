package app.munch.sitemap.profile;

import app.munch.model.ArticleStatus;
import app.munch.sitemap.SitemapProvider;
import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 12:14 pm
 */
@Singleton
public final class ArticleSitemap implements SitemapProvider {

    private final TransactionProvider provider;

    @Inject
    ArticleSitemap(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String name() {
        return "articles";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() {
        return provider.reduce(true, entityManager -> {
            return Iterators.transform(select(entityManager), tuple -> {
                Objects.requireNonNull(tuple);
                String username = tuple.get("username", String.class);
                String slug = tuple.get("slug", String.class);
                String id = tuple.get("id", String.class);
                Date updatedAt = tuple.get("updatedAt", Date.class);

                return build("/@" + username + "/" + slug + "-" + id, updatedAt, 1.0, ChangeFreq.WEEKLY);
            });
        });
    }

    private Iterator<Tuple> select(EntityManager entityManager) {
        return entityManager.createQuery("SELECT " +
                "a.profile.username as username, " +
                "a.slug as slug, " +
                "a.id as id, " +
                "a.updatedAt as updatedAt " +
                "FROM Article a " +
                "WHERE a.status = :status", Tuple.class)
                .setParameter("status", ArticleStatus.PUBLISHED)
                .getResultList()
                .iterator();
    }
}
