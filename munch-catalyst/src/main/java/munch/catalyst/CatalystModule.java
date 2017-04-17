package munch.catalyst;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 10:02 PM
 * Project: munch-core
 */
public class CatalystModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load().getConfig("catalyst");
    }
}
