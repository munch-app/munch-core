package munch.api.services.places.loader;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Created by: Fuxing
 * Date: 6/3/18
 * Time: 8:59 PM
 * Project: munch-core
 */
public class PlaceDataLoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<PlaceDataCardLoader> binder = Multibinder.newSetBinder(binder(), PlaceDataCardLoader.class);
        binder.addBinding().to(PlaceAwardCardLoader.class);
        binder.addBinding().to(PlaceMenuCardLoader.class);
    }
}
