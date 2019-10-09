package app.munch.sitemap.profile;

import app.munch.sitemap.SitemapProvider;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.util.Iterator;

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
    public Iterator<WebSitemapUrl> provide() throws MalformedURLException {
        // TODO(fuxing): all public media
        return null;
    }
}
