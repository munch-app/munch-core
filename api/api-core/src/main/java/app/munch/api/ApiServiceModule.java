package app.munch.api;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import dev.fuxing.transport.service.TransportService;
import munch.api.ApiService;
import munch.api.ObjectCleaner;
import munch.restful.server.RestfulService;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:07 PM
 * Project: munch-core
 */
public abstract class ApiServiceModule extends AbstractModule {

    /**
     * @param clazz of ApiService to add
     */
    @Deprecated
    protected <T extends ApiService> void addDeprecatedService(Class<T> clazz) {
        Multibinder<RestfulService> binder = Multibinder.newSetBinder(binder(), RestfulService.class);
        binder.addBinding().to(clazz);
    }

    protected <T extends TransportService> void addService(Class<T> clazz) {
        Multibinder<TransportService> binder = Multibinder.newSetBinder(binder(), TransportService.class);
        binder.addBinding().to(clazz);
    }

    /**
     * @param clazz of HealthCheck to add
     */
    protected <T extends ApiHealthCheck> void addHealth(Class<T> clazz) {
        Multibinder<ApiHealthCheck> binder = Multibinder.newSetBinder(binder(), ApiHealthCheck.class);
        binder.addBinding().to(clazz);
    }

    /**
     * @param clazz of Cleaner to add
     */
    @Deprecated
    protected <T extends ObjectCleaner> void addCleaner(Class<T> clazz) {
        Multibinder<ObjectCleaner> binder = Multibinder.newSetBinder(binder(), ObjectCleaner.class);
        binder.addBinding().to(clazz);
    }
}
