package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.CatalystModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import corpus.data.DataModule;
import munch.catalyst.clients.ClientModule;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 10:02 PM
 * Project: munch-core
 */
public class MunchModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new CatalystModule());
        install(new DataModule());
        install(new ClientModule());
        bind(CatalystEngine.class).to(MunchCatalyst.class);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MunchModule());
        injector.getInstance(MunchCatalyst.class).run();
    }
}
