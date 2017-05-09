package munch.places.menu;

import com.google.inject.*;
import com.typesafe.config.ConfigFactory;
import munch.places.menu.data.PostgresModule;
import munch.restful.server.RestfulServer;
import munch.restful.server.RestfulService;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:48 PM
 * Project: munch-core
 */
@Singleton
public final class MenuApi extends RestfulServer {

    @Inject
    public MenuApi(Set<RestfulService> services) {
        super(services);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new PostgresModule(),
                getPersistModule(),
                new MenuModule()
        );

        // Start restful server on config port
        final RestfulServer server = injector.getInstance(MenuApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }

    /**
     * Aws or Fake
     *
     * @return Persist module to use
     */
    private static Module getPersistModule() {
        switch (ConfigFactory.load().getString("menu.persist")) {
            case "aws":
                return new AWSModule();
            default:
                return new FakeModule();
        }
    }
}
