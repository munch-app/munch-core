package munch.places;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.JsonUtils;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:48 PM
 * Project: munch-core
 */
public class MenuModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        return JsonUtils.objectMapper;
    }
}
