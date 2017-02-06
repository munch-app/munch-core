package com.munch.accounts.controller;

import com.munch.accounts.spark.SparkServer;
import org.pac4j.sparkjava.SecurityFilter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 4/2/2017
 * Time: 6:33 PM
 * Project: munch-core
 */
public class AccountController extends SparkServer.Controller {

    @Override
    public void route() {
        Spark.post("/email/verify", this::verifyEmail, templateEngine);

        Spark.before("/user/*", new SecurityFilter(pacConfig, null));
        Spark.get("/user/account", this::viewAccount, templateEngine);
        Spark.post("/user/account", this::updateAccount);
    }

    // TODO 3 actions

    private ModelAndView verifyEmail(Request request, Response response) {
        Map<String, Object> map = new HashMap<>();
        return new ModelAndView(map, "verifiedEmail.hbs");
    }

    private ModelAndView viewAccount(Request request, Response response) {
        Map<String, Object> map = new HashMap<>();
        return new ModelAndView(map, "account.hbs");
    }

    private Void updateAccount(Request request, Response response) {
        response.redirect("/user/account");
        return null;
    }
}
