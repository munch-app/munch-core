package munch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.api.clients.ClientModule;
import munch.api.services.ServiceModule;
import munch.restful.server.JsonService;
import munch.restful.server.RestfulServer;

/**
 * Created by: Fuxing
 * Date: 25/3/2017
 * Time: 7:45 PM
 * Project: munch-core
 */
public class ApiModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ClientModule());
        install(new ServiceModule());
    }

    @Provides
    ObjectMapper provideObjectMapper() {
        return JsonService.objectMapper;
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load().getConfig("api");
    }

    /**
     * Start api server with predefined modules
     *
     * @param args not required
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ApiModule());
        final RestfulServer server = injector.getInstance(ApiServer.class);
        server.start(ConfigFactory.load().getInt("http.port"));
    }
}
