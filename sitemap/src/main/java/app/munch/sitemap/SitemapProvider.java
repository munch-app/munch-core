package app.munch.sitemap;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 8:53 AM
 * Project: munch-core
 */
public interface SitemapProvider {

    /**
     * @return name of sitemap
     */
    String name();

    Iterator<WebSitemapUrl> provide() throws MalformedURLException;

    default WebSitemapUrl build(String path) {
        try {
            return new WebSitemapUrl.Options(baseUrl(path)).build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    default WebSitemapUrl build(String path, double priority, ChangeFreq freq) {
        return build(path, new Date(), priority, freq);
    }

    default WebSitemapUrl build(String path, Date date, double priority, ChangeFreq freq) {
        try {
            return new WebSitemapUrl.Options(baseUrl(path))
                    .lastMod(date)
                    .priority(priority)
                    .changeFreq(freq)
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    default String baseUrl(String path) {
        return baseUrl() + path;
    }

    default String baseUrl() {
        return "https://www.munch.app";
    }
}
