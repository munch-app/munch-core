package app.munch.sitemap.profile;

import app.munch.model.ProfileMediaStatus;
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
 * @author Fuxing Loh
 * @since 2019-10-09 at 2:00 am
 */
@Singleton
public final class MediaSitemap implements SitemapProvider {

    private final TransactionProvider provider;

    @Inject
    MediaSitemap(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String name() {
        return "medias";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() {
        return provider.reduce(true, entityManager -> {
            Iterator<WebSitemapUrl> iterator = Iterators.transform(select(entityManager), tuple -> {
                Objects.requireNonNull(tuple);
                String id = tuple.get("id", String.class);
                String username = tuple.get("username", String.class);
                Date createdAt = tuple.get("createdAt", Date.class);

                return build("/@" + username + "/" + id, createdAt, 1.0, ChangeFreq.WEEKLY);
            });

            return Iterators.filter(iterator, Objects::nonNull);
        });
    }

    private Iterator<Tuple> select(EntityManager entityManager) {
        return entityManager.createQuery("SELECT " +
                "m.id as id, " +
                "m.profile.username as username, " +
                "m.createdAt as createdAt " +
                "FROM ProfileMedia m " +
                "WHERE status = :status", Tuple.class)
                .setParameter("status", ProfileMediaStatus.PUBLIC)
                .getResultList()
                .iterator();
    }
}
