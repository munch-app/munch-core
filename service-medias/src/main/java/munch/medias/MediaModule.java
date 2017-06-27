package munch.medias;

import com.google.inject.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.medias.hibernate.PostgresModule;
import munch.restful.server.RestfulServer;
import spark.Spark;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 6:59 PM
 * Project: munch-core
 */
public final class MediaModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PostgresModule());
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    @Singleton
    private static final class Server extends RestfulServer {
        @Inject
        public Server(MediaService service) {
            super(service);
        }

        @Override
        public void start(int port) {
            super.start(port);
            Spark.before((request, response) -> logger.info("{}: {}", request.requestMethod(), request.pathInfo()));
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MediaModule());
        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(Server.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
