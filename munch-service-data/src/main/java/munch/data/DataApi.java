package munch.data;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.data.place.PlaceService;
import munch.restful.server.RestfulServer;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:09 PM
 * Project: munch-core
 */
@Singleton
public final class DataApi extends RestfulServer {

    @Inject
    public DataApi(PlaceService placeService) {
        super(placeService);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new PostgresModule()
        );

        Config config = injector.getInstance(Config.class);
        final int port = config.getInt("http.port");
        final RestfulServer server = injector.getInstance(DataApi.class);
        server.start(port);
    }
}
