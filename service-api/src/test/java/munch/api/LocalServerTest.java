package munch.api;

import com.google.inject.Guice;
import com.google.inject.Injector;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 21/7/2017
 * Time: 7:08 PM
 * Project: munch-core
 */
public class LocalServerTest extends ApiModule {

    private static void injectSystem() {
        System.setProperty("services.data.url", "http://localhost:8700");
        System.setProperty("services.search.url", "http://localhost:8701");
        System.setProperty("services.nominatim.url", "http://localhost:9888");
    }

    public static void main(String[] args) {
        injectSystem();

        Injector injector = Guice.createInjector(new ApiModule());
        final RestfulServer server = injector.getInstance(ApiServer.class);
        server.start(8888);
    }
}
