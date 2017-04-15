package munch.geocoder;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 2:55 PM
 * Project: munch-core
 */
public class GeocoderModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }
}
