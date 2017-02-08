package com.munch.accounts.controller;

import com.munch.utils.spark.SparkController;
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
public class AccountController implements SparkController {

    @Override
    public void route() {
        Spark.post("/email/verify", this::verifyEmail, templateEngine);

//        Spark.before("/account", new SecurityFilter(pacConfig, null));
//        Spark.before("/account/*", new SecurityFilter(pacConfig, null));
//        Spark.get("/account", this::viewAccount, templateEngine);
//        Spark.post("/account", this::updateAccount);
    }

    // TODO verify email

    private ModelAndView verifyEmail(Request request, Response response) {
        Map<String, Object> map = new HashMap<>();
        return new ModelAndView(map, "verifiedEmail.hbs");
    }

//    private ModelAndView viewAccount(Request request, Response response) {
//        Map<String, Object> map = new HashMap<>();
//        return new ModelAndView(map, "account.hbs");
//    }
//
//    private Void updateAccount(Request request, Response response) {
//        response.redirect("/account");
//        return null;
//    }
}
