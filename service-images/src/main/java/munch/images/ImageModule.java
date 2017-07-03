package munch.images;

import com.google.inject.*;
import com.squareup.pollexor.Thumbor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;

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
