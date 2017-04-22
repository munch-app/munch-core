package munch.places.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.JsonUtils;
import munch.restful.server.RestfulService;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 3:48 PM
 * Project: munch-core
 */
public class MenuModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<RestfulService> routerBinder = Multibinder.newSetBinder(binder(), RestfulService.class);
        routerBinder.addBinding().to(MenuService.class);

        // Only load upload service if menu.thumbor.url path exist
        if (ConfigFactory.load().hasPath("menu.thumbor.url"))
            routerBinder.addBinding().to(UploadService.class);
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
