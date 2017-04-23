package munch.catalyst.data;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 10:02 PM
 * Project: munch-core
 */
public class DataModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<DataConsumer> routerBinder = Multibinder.newSetBinder(binder(), DataConsumer.class);
        routerBinder.addBinding().to(PlaceConsumer.class);
    }

}
