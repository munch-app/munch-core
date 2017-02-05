package com.munch.accounts.controller;

import com.munch.accounts.spark.SparkServer;
import org.apache.commons.lang3.StringUtils;
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

        // Login Callback
        CallbackRoute callback = new CallbackRoute(pacConfig, null, true);
        Spark.get("/callback", callback);
        Spark.post("/callback", callback);

        // Login Page/Form
        Spark.get("/login", this::viewLogin, templateEngine);

        // Login Provider
        Spark.before("/login/facebook", new SecurityFilter(pacConfig, "FacebookClient"));
        Spark.before("/login/email", new SecurityFilter(pacConfig, "FormClient"));
        Spark.get("/login/facebook", this::redirect);
        Spark.get("/login/email", this::redirect);

        Spark.before("/user/*", new SecurityFilter(pacConfig, null));
        Spark.get("/user/account", this::viewAccount, templateEngine);
        Spark.post("/user/account", this::updateAccount);

        Spark.get("/logout", new ApplicationLogoutRoute(pacConfig, "/???"));
    }

    /**
     * Redirect to /user/account or
     * partner.munchapp.co/callback
     *
     * @param request request
     * @param response response
     * @return null
     */
    private Void redirect(Request request, Response response) {
        String to = request.queryParams("to");
        if (StringUtils.isNotBlank(to)) {
            if (to.equalsIgnoreCase("partner")) {
                // TODO real redirect implementation
                response.redirect("https://partner.munchapp.co/callback?");
            }
        } else {
            response.redirect("/user/account");
        }
        return null;
    }

    private ModelAndView viewLogin(Request request, Response response) {
        return null;
    }

    private ModelAndView viewAccount(Request request, Response response) {
        return null;
    }

    private Void updateAccount(Request request, Response response) {
        return null;
    }
}
