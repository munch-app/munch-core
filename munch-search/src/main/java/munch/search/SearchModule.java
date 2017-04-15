package munch.search;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.RestfulService;

/**
 * Created by: Fuxing
 * Date: 16/4/2017
 * Time: 6:41 AM
 * Project: munch-core
 */
public class SearchModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<RestfulService> routerBinder = Multibinder.newSetBinder(binder(), RestfulService.class);
        routerBinder.addBinding().to(IndexService.class);
        routerBinder.addBinding().to(SearchService.class);
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }
}
