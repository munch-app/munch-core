package munch.api.endpoints.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Named;

/**
 * Created by: Fuxing
 * Date: 15/4/2017
 * Time: 3:44 AM
 * Project: munch-core
 */
public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Named("services")
    @Provides
    Config provideConfig() {
        return ConfigFactory.load().getConfig("services");
    }

}
