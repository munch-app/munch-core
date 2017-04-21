package munch.places;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:48 PM
 * Project: munch-core
 */
@Singleton
public final class MenuApi extends RestfulServer {

    @Inject
    public MenuApi(MenuService menu, PlaceService place) {
        super(menu, place);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new MenuModule()
        );

        // Start restful server on config port
        final RestfulServer server = injector.getInstance(MenuApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
