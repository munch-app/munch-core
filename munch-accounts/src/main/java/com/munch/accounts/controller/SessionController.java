package com.munch.accounts.controller;

import com.munch.accounts.spark.SparkServer;

/**
 * Created by: Fuxing
 * Date: 4/2/2017
 * Time: 6:33 PM
 * Project: munch-core
 */
public class SessionController extends SparkServer.Controller {

    @Override
    public void route() {

    }

    private void loginEmail(String email, char[] password) {
        // TODO email login
    }

    private void loginFacebook(char[] facebookToken) {
        // TODO once mobile api is work in progress
    }

    // Login Instagram should not be required as it is partner only

    private void logout(String token) {

    }
}
