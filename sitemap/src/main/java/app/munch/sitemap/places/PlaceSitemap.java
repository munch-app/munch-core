package app.munch.sitemap.places;

import app.munch.model.Place;
import app.munch.model.StatusType;
import app.munch.sitemap.SitemapProvider;
import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
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
        return provider.reduce(entityManager -> {
            Iterator<WebSitemapUrl> iterator = Iterators.transform(entityManager.createQuery("FROM Place", Place.class)
                    .getResultList()
                    .iterator(), place -> {
                Objects.requireNonNull(place);

                if (place.getStatus().getType() == StatusType.OPEN) {
                    return build("/" + place.getSlug() + "-" + place.getId(), place.getUpdatedAt(), place.getImportant(), ChangeFreq.DAILY);
                }

                return null;
            });

            return Iterators.filter(iterator, Objects::nonNull);
        });
    }
}
