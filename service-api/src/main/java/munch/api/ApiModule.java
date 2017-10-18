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
import munch.data.dynamodb.DynamoModule;
import munch.data.elastic.ElasticModule;
import munch.restful.server.JsonService;

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
        install(new DynamoModule());
        install(new ElasticModule());
    }

    @Provides
    ObjectMapper provideObjectMapper() {
        return JsonService.objectMapper;
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    /**
     * Start api server with predefined modules
     *
     * @param args not required
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ApiModule());
        injector.getInstance(ApiServer.class).start();
    }
}
