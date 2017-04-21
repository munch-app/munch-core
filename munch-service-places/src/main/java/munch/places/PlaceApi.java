package munch.places;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import munch.places.data.PostgresModule;
import munch.places.search.ElasticModule;
import munch.restful.server.RestfulServer;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:09 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceApi extends RestfulServer {

    @Inject
    public PlaceApi(DataService data, SearchService search, ImageLinkService image, MetaService meta) {
        super(data, search, image, meta);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new PlaceModule(),
                new PostgresModule(),
                new ElasticModule()
        );

        // Start restful server on config port
        final RestfulServer server = injector.getInstance(PlaceApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
