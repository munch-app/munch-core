package munch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.JsonUtils;

/**
 * Created by: Fuxing
 * Date: 25/3/2017
 * Time: 7:45 PM
 * Project: munch-core
 */
public class ApiModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    ObjectMapper provideObjectMapper() {
        return JsonUtils.objectMapper;
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load().getConfig("api");
    }
}
