package com.munch.accounts;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.munch.accounts.controller.ControllerModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 7:30 PM
 * Project: munch-core
 */
public class WebsiteServer {

    // Testing Controller only websites
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new H2MemModule(),
                new AccountsModule(),
                new ControllerModule()
        );

        Config config = ConfigFactory.load().getConfig("munch.accounts");
        injector.getInstance(AccountsServer.class).start(config.getInt("http.port"));
    }

}
