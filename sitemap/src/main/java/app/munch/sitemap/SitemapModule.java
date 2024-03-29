package app.munch.sitemap;

import app.munch.database.DatabaseModule;
import app.munch.sitemap.places.PlaceSitemap;
import app.munch.sitemap.profile.ArticleSitemap;
import app.munch.sitemap.profile.MediaSitemap;
import app.munch.sitemap.profile.ProfileSitemap;
import catalyst.utils.health.HealthCheckServer;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;

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
        binder.addBinding().to(ProfileSitemap.class);
        binder.addBinding().to(ArticleSitemap.class);
        binder.addBinding().to(MediaSitemap.class);

        binder.addBinding().to(PlaceSitemap.class);

        install(new AmazonModule());
        install(new DatabaseModule());
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
