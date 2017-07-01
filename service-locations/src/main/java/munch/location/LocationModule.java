package munch.location;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.typesafe.config.ConfigFactory;
import munch.location.database.DatabaseModule;
import munch.restful.server.RestfulServer;

import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 14/6/2017
 * Time: 9:12 PM
 * Project: munch-core
 */
public class LocationModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DatabaseModule());
    }

    @Singleton
    public static final class Server extends RestfulServer {
        @Inject
        public Server(LocationService service) {
            super(service);
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new LocationModule());
        final RestfulServer server = injector.getInstance(Server.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
