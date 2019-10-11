package app.munch.sitemap.profile;

import app.munch.model.Profile;
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
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 12:14 pm
 */
@Singleton
public final class ProfileSitemap implements SitemapProvider {

    private final TransactionProvider provider;

    @Inject
    ProfileSitemap(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public String name() {
        return "profiles";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() {
        return provider.reduce(true, entityManager -> {
            Iterator<List<WebSitemapUrl>> iterator = Iterators.transform(select(entityManager), tuple -> {
                Objects.requireNonNull(tuple);
                String uid = tuple.get("uid", String.class);
                String username = tuple.get("username", String.class);
                Date updatedAt = tuple.get("updatedAt", Date.class);

                if (Profile.ALL_SPECIAL_ID.contains(uid)) {
                    return List.of();
                }

                return List.of(
                        build("/@" + username, updatedAt, 1.0, ChangeFreq.DAILY),
                        build("/@" + username + "/articles", updatedAt, 1.0, ChangeFreq.DAILY),
                        build("/@" + username + "/medias", updatedAt, 1.0, ChangeFreq.DAILY)
                );
            });

            Iterator<Iterator<WebSitemapUrl>> transform = Iterators.transform(iterator, List::iterator);
            return Iterators.concat(transform);
        });
    }

    private Iterator<Tuple> select(EntityManager entityManager) {
        return entityManager.createQuery("SELECT " +
                "p.uid as uid, " +
                "p.username as username, " +
                "p.updatedAt as updatedAt " +
                "FROM Profile p", Tuple.class)
                .getResultList()
                .iterator();
    }
}
