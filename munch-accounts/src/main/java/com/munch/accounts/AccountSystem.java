package com.munch.accounts;

import com.munch.accounts.controller.AccountController;
import com.munch.accounts.controller.AssetsController;
import com.munch.accounts.controller.SessionController;
import com.munch.accounts.spark.SparkServer;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 1:58 AM
 * Project: munch-core
 */
public class AccountSystem {

    /**
     * @return all the controllers account system uses
     */
    static SparkServer.Controller[] controllers() {
        return new SparkServer.Controller[]{
                new AssetsController(),
                new SessionController(),
                new AccountController()
        };
    }

    public static void main(String[] args) {
        SparkServer server = new SparkServer(controllers());
        server.start();
    }
}
