package app.munch.sitemap.search;

import app.munch.sitemap.SitemapProvider;
import com.redfin.sitemapgenerator.WebSitemapUrl;

import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * @author Fuxing Loh
 * @since 2019-10-17 at 5:57 pm
 */
@Singleton
public final class LocationSitemap implements SitemapProvider {

    @Override
    public String name() {
        return "locations";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() throws MalformedURLException {
        return null;
    }
}
