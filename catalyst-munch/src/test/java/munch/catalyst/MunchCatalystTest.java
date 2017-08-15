package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.CatalystModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import corpus.data.DataModule;

/**
 * Created By: Fuxing Loh
 * Date: 13/6/2017
 * Time: 6:24 PM
 * Project: munch-core
 */
class MunchCatalystTest {

    public static void main(String[] args) {
        System.setProperty("services.catalyst.data.url", "http://edge.corpus.munch.space:8200");
        // Token is injected with env

        Injector injector = Guice.createInjector(new TestModule());
        injector.getInstance(MunchCatalyst.class).run();
    }

    static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            install(new CatalystModule());
            install(new DataModule());
            install(new EmptyClientModule());
            bind(CatalystEngine.class).to(MunchCatalyst.class);
        }
    }
}