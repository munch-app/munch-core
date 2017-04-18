package munch.gallery;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 6:59 PM
 * Project: munch-core
 */
public class GalleryModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }
}
