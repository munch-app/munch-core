package com.munch.accounts;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.munch.accounts.controller.ControllerModule;
import com.munch.utils.spark.SparkRouter;
import com.munch.utils.spark.SparkServer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 1:58 AM
 * Project: munch-core
 */
public class AccountSystem {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AccountsModule(), new ControllerModule());

        Config config = ConfigFactory.load().getConfig("munch.accounts");
        injector.getInstance(AccountsServer.class).start(config.getInt("http.port"));
    }

    @Singleton
    public static class AccountsServer extends SparkServer {

        @Inject
        public AccountsServer(Set<SparkRouter> routers) {
            super(routers);
        }

    }
}
