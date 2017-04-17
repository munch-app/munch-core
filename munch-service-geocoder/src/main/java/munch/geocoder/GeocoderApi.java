package munch.geocoder;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.typesafe.config.ConfigFactory;
import munch.geocoder.database.DatabaseModule;
import munch.restful.server.RestfulServer;

import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 2:51 PM
 * Project: munch-core
 */
@Singleton
public final class GeocoderApi extends RestfulServer {

    @Inject
    public GeocoderApi(GeocoderService service) {
        super(service);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new DatabaseModule()
        );

        // Start server on default port in setting = http.port
        final RestfulServer server = injector.getInstance(GeocoderApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
