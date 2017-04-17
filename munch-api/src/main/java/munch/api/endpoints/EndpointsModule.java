package munch.api.endpoints;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import munch.restful.server.RestfulService;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 10:46 PM
 * Project: munch-core
 */
public class EndpointsModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<RestfulService> routerBinder = Multibinder.newSetBinder(binder(), RestfulService.class);
        routerBinder.addBinding().to(PlaceEndpoint.class);
        routerBinder.addBinding().to(NeighborhoodEndpoint.class);
    }

}
