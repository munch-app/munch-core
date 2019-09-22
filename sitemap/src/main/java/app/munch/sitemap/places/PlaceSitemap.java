package app.munch.sitemap.places;

import app.munch.model.Status;
import app.munch.model.StatusType;
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
 * Date: 3/11/18
 * Time: 12:27 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceSitemap implements SitemapProvider {

    private final TransactionProvider provider;

    @Inject
    PlaceSitemap(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String name() {
        return "places";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() {
        return provider.reduce(true, entityManager -> {
            Iterator<WebSitemapUrl> iterator = Iterators.transform(select(entityManager), tuple -> {
                Objects.requireNonNull(tuple);

                String id = tuple.get("id", String.class);
                String slug = tuple.get("slug", String.class);
                Status status = tuple.get("status", Status.class);
                Date updatedAt = tuple.get("updatedAt", Date.class);
                Double important = tuple.get("important", Double.class);

                if (status.getType() == StatusType.OPEN) {
                    return build("/" + slug + "-" + id, updatedAt, important, ChangeFreq.DAILY);
                }

                return null;
            });

            return Iterators.filter(iterator, Objects::nonNull);
        });
    }

    private Iterator<Tuple> select(EntityManager entityManager) {
        return entityManager.createQuery("SELECT " +
                "p.id as id, " +
                "p.slug as slug, " +
                "p.status as status, " +
                "p.updatedAt as updatedAt, " +
                "p.important as important " +
                "FROM Place p", Tuple.class)
                .getResultList()
                .iterator();
    }
}
