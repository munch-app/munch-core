package munch.articles;

import com.google.inject.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.clients.ClientsModule;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:22 AM
 * Project: munch-core
 */
public class ArticleModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ClientsModule());
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    @Singleton
    public static class Server extends RestfulServer {
        @Inject
        public Server(ArticleService service) {
            super(service);
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ArticleModule());
        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(Server.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
