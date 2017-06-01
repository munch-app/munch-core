package munch.images;

import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.squareup.pollexor.Thumbor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:22 AM
 * Project: munch-core
 */
public class ImageModule extends AbstractModule {

    @Override
    protected void configure() {
        Config config = ConfigFactory.load();
        if (config.getString("image.persist").equals("aws")) {
            install(new AWSModule());
        } else {
            install(new FakeModule());
        }

        Multibinder<RestfulService> routerBinder = Multibinder.newSetBinder(binder(), RestfulService.class);
        routerBinder.addBinding().to(ImageService.class);

        // Only load put service if image.thumbor.url config path exist
        if (ConfigFactory.load().hasPath("image.thumbor.url"))
            routerBinder.addBinding().to(PutService.class);
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    @Provides
    @Singleton
    Thumbor provideThumbor(Config config) {
        return Thumbor.create(config.getString("image.thumbor.url"));
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ImageModule());
        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(ImageApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
