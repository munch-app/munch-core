package munch.instagram;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.instagram.hibernate.PostgresModule;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 6:59 PM
 * Project: munch-core
 */
public final class InstagramModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PostgresModule());
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    public static void main(String[] args) {
        // Start server on default port in setting = http.port
        Injector injector = Guice.createInjector(new InstagramModule());
        final RestfulServer server = injector.getInstance(InstagramApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
