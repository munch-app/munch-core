package app.munch.sitemap.profile;

import app.munch.model.Profile;
import app.munch.sitemap.SitemapProvider;
import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.util.Iterator;
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
    public Iterator<WebSitemapUrl> provide() throws MalformedURLException {
        return provider.reduce(entityManager -> {
            Iterator<WebSitemapUrl> iterator = Iterators.transform(entityManager.createQuery("FROM Profile", Profile.class)
                    .getResultList()
                    .iterator(), profile -> {
                Objects.requireNonNull(profile);

                if (Profile.ALL_SPECIAL_ID.contains(profile.getUid())) {
                    return null;
                }

                return build("/@" + profile.getUsername(), profile.getUpdatedAt(), 1.0, ChangeFreq.DAILY);
            });

            return Iterators.filter(iterator, Objects::nonNull);
        });
    }
}
