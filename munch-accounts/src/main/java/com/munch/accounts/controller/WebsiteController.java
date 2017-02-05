package com.munch.accounts.controller;

import com.munch.accounts.spark.SparkServer;
import org.pac4j.core.config.Config;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Created by: Fuxing
 * Date: 4/2/2017
 * Time: 6:38 PM
 * Project: munch-core
 */
public class WebsiteController extends SparkServer.Controller {

    // Show account page to edit details
    // Show login page and redirect

    private final Config pacConfig;

    public WebsiteController() {
        this.pacConfig = new PacConfigFactory(templateEngine).build();
    }

    @Override
    public void route() {
        CallbackRoute callback = new CallbackRoute(pacConfig, null, true);

        Spark.get("/callback", callback);
        Spark.post("/callback", callback);

        // Login Page/Form
        Spark.get("/login", this::loginPage, templateEngine);

        // Login Provider
        Spark.before("/login/facebook", new SecurityFilter(pacConfig, "FacebookClient"));
        Spark.before("/login/email", new SecurityFilter(pacConfig, "FormClient"));
        Spark.get("/login/facebook", this::redirect);
        Spark.get("/login/email", this::redirect);

        // TODO naming
        Spark.before("/account", new SecurityFilter(pacConfig, null));
        Spark.get("/account", this::loginPage, templateEngine); // TODO

        Spark.get("/logout", new ApplicationLogoutRoute(pacConfig, "/???"));
    }

    public String redirect(Request request, Response response) {
        // TODO: Check where to redirect to?
        response.redirect("");
        return "redirect to";
    }

    public ModelAndView loginPage(Request request, Response response) {
        return null;
    }

    public void login() {

    }

    public void logout() {

    }
}
