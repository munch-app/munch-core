package munch.search;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;
import munch.search.place.PlaceModule;
import munch.search.place.PlaceService;

/**
 * Created by: Fuxing
 * Date: 16/4/2017
 * Time: 6:41 AM
 * Project: munch-core
 */
@Singleton
public final class SearchApi extends RestfulServer {

    @Inject
    public SearchApi(PlaceService service) {
        super(service);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ElasticModule(),
                new PlaceModule()
        );

        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(SearchApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
