package munch.api.services.curator;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

/**
 * Created by: Fuxing
 * Date: 11/7/2017
 * Time: 11:06 AM
 * Project: munch-core
 */
@Singleton
public class CuratorModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Curator> curatorBinder = Multibinder.newSetBinder(binder(), Curator.class);
        curatorBinder.addBinding().to(SingaporeCurator.class);
        curatorBinder.addBinding().to(ExplicitLocationCurator.class);
        curatorBinder.addBinding().to(ImplicitLocationCurator.class);
        curatorBinder.addBinding().to(NonTabCurator.class);
    }
}
