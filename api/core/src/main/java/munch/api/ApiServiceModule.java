package munch.api;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import munch.restful.server.RestfulService;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:07 PM
 * Project: munch-core
 */
public abstract class ApiServiceModule extends AbstractModule {

    protected <T extends ApiService> void addService(Class<T> clazz) {
        Multibinder<RestfulService> binder = Multibinder.newSetBinder(binder(), RestfulService.class);
        binder.addBinding().to(clazz);
    }

    protected <T extends HealthCheck> void addHealth(Class<T> clazz) {
        Multibinder<HealthCheck> binder = Multibinder.newSetBinder(binder(), HealthCheck.class);
        binder.addBinding().to(clazz);
    }

    protected <T extends ObjectCleaner> void addCleaner(Class<T> clazz) {
        Multibinder<ObjectCleaner> binder = Multibinder.newSetBinder(binder(), ObjectCleaner.class);
        binder.addBinding().to(clazz);
    }
}
