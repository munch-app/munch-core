package com.munch.accounts.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.munch.utils.spark.AssetsController;
import com.munch.utils.spark.SparkRouter;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:20 PM
 * Project: munch-core
 */
public class ControllerModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<SparkRouter> routerBinder = Multibinder.newSetBinder(binder(), SparkRouter.class);
        routerBinder.addBinding().to(AssetsController.class);
        routerBinder.addBinding().to(AccountController.class);
        routerBinder.addBinding().to(SessionController.class);
    }

    /**
     * @return /public assets controller
     */
    @Provides
    AssetsController providesAssets() {
        return new AssetsController("/public");
    }
}
