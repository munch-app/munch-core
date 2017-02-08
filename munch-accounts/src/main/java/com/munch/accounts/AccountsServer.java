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
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 7:24 PM
 * Project: munch-core
 */
@Singleton
public final class AccountsServer extends SparkServer {

    @Inject
    public AccountsServer(Set<SparkRouter> routers) {
        super(routers);
    }

    /**
     * Starting point for accounts erver
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new PostgresModule(),
                new AccountsModule(),
                new ControllerModule()
        );

        Config config = ConfigFactory.load().getConfig("munch.accounts");
        injector.getInstance(AccountsServer.class).start(config.getInt("http.port"));
    }
}
