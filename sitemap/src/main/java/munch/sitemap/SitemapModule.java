package munch.sitemap;

import catalyst.utils.health.HealthCheckServer;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import munch.sitemap.creator.CreatorSitemap;
import munch.sitemap.places.PlaceSitemap;
import munch.sitemap.sgp.SGPSearchSitemap;

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
        binder.addBinding().to(PlaceSitemap.class);
        binder.addBinding().to(CreatorSitemap.class);

        install(new AmazonModule());
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SitemapModule());
        SitemapGenerator generator = injector.getInstance(SitemapGenerator.class);

        HealthCheckServer.startBlocking(() -> {
            try {
                generator.uploadGenerated();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
