package munch.images;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.images.persist.PersistService;
import munch.restful.server.RestfulService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:22 AM
 * Project: munch-core
 */
public class ImageModule extends AbstractModule {

    @Override
    protected void configure() {
        // TOOD configure all other services
        Multibinder<RestfulService> routerBinder = Multibinder.newSetBinder(binder(), RestfulService.class);
        routerBinder.addBinding().to(ResolveService.class);
        // Only load if true for config image.persist
        if (ConfigFactory.load().getBoolean("image.persist"))
            routerBinder.addBinding().to(PersistService.class);
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }
}
