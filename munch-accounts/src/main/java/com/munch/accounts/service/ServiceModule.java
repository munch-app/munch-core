package com.munch.accounts.service;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.munch.utils.spark.SparkRouter;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 2:20 PM
 * Project: munch-core
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<SparkRouter> routerBinder = Multibinder.newSetBinder(binder(), SparkRouter.class);
        routerBinder.addBinding().to(AccountService.class);
        routerBinder.addBinding().to(SessionService.class);
    }

}
