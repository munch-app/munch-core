package munch.api;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 21/7/2017
 * Time: 7:08 PM
 * Project: munch-core
 */
public class LocalServerTest extends ApiModule {

    private static void injectSystem() {
        System.setProperty("services.places.url", "http://localhost:8700");
        System.setProperty("services.articles.url", "http://localhost:8703");
        System.setProperty("services.instagram.url", "http://localhost:8704");

        System.setProperty("services.search.url", "http://localhost:8701");
        System.setProperty("services.images.url", "http://localhost:8702");
        System.setProperty("services.nominatim.url", "http://localhost:9888");
    }

    public static void main(String[] args) {
        injectSystem();

        Injector injector = Guice.createInjector(new ApiModule());
        final RestfulServer server = injector.getInstance(ApiServer.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
