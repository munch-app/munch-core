package munch.api.endpoints;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 10:46 PM
 * Project: munch-core
 */
public class EndpointsModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<MunchEndpoint> routerBinder = Multibinder.newSetBinder(binder(), MunchEndpoint.class);
        routerBinder.addBinding().to(DiscoverEndpoint.class);
    }
}
