package munch.places;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.places.data.PostgresModule;
import munch.places.elastic.ElasticModule;
import munch.restful.server.JsonService;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 4:11 PM
 * Project: munch-core
 */
public class PlaceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PostgresModule());
        install(new ElasticModule());
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        return JsonService.objectMapper;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new PlaceModule());

        // Start restful server on config port
        final RestfulServer server = injector.getInstance(PlaceApi.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
