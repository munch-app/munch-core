package munch.sitemap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import com.munch.utils.file.ContentTypeError;
import munch.sitemap.sgp.SGPSearchSitemap;

import java.net.MalformedURLException;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 8:51 AM
 * Project: munch-core
 */
public final class SitemapModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<SitemapProvider> binder = Multibinder.newSetBinder(binder(), SitemapProvider.class);
        binder.addBinding().to(SGPSearchSitemap.class);

        install(new AmazonModule());
    }

    public static void main(String[] args) throws MalformedURLException, ContentTypeError {
        Injector injector = Guice.createInjector(new SitemapModule());
        SitemapGenerator generator = injector.getInstance(SitemapGenerator.class);
        generator.uploadGenerated();
    }
}
