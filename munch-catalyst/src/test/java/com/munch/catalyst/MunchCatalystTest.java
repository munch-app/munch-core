package com.munch.catalyst;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

/**
 * Created by: Fuxing
 * Date: 8/3/2017
 * Time: 1:59 AM
 * Project: munch-core
 */
class MunchCatalystTest {

    @Test
    void run() throws Exception {
        Injector injector = Guice.createInjector(
//                new H2FileModule()
        );

        MunchCatalyst catalyst = injector.getInstance(MunchCatalyst.class);
        catalyst.run();
    }
}