package com.munch.accounts.controller;

import com.munch.accounts.spark.SparkServer;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.sparkjava.ApplicationLogoutRoute;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 4/2/2017
 * Time: 6:38 PM
 * Project: munch-core
 */
public class SessionController extends SparkServer.Controller {

    // Show account page to edit details
    // Show login page and redirect

    private final FormClient formClient;

    public SessionController() {
        this.formClient = pacConfig.getClients().findClient(FormClient.class);
    }

    @Override
    public void route() {
        // Index page redirect
        Spark.get("/", this::index);

        // Login Callback
        CallbackRoute callback = new CallbackRoute(pacConfig, null, true);
        Spark.get("/callback", callback);
        Spark.post("/callback", callback);
        // TODO facebook auth on callback

        Spark.get("/create", this::viewCreate, templateEngine);
        Spark.post("/create", this::create);

        // Login Page/Form
        Spark.get("/login", this::viewLogin, templateEngine);

        // Login Provider
        Spark.before("/login/facebook", new SecurityFilter(pacConfig, "FacebookClient"));
        Spark.before("/login/email", new SecurityFilter(pacConfig, "FormClient"));
        Spark.get("/login/facebook", this::redirect);
        Spark.get("/login/email", this::redirect);

        Spark.get("/logout", new ApplicationLogoutRoute(pacConfig, "/???"));
    }

    /**
     * Redirect to /account if user logged in
     * or /login if user not logged in
     *
     * @param request  request
     * @param response response
     * @return null
     */
    private Void index(Request request, Response response) {
        SparkWebContext context = new SparkWebContext(request, response);
        ProfileManager manager = new ProfileManager(context);
        if (manager.isAuthenticated()) {
            response.redirect("/account");
        } else {
            response.redirect("/login");
        }
        return null;
    }

    /**
     * Redirect to /account or
     * https://partner.munchapp.co/callback
     *
     * @param request  request
     * @param response response
     * @return null
     */
    private Void redirect(Request request, Response response) {
        String to = request.queryParams("to");
        if (StringUtils.isNotBlank(to)) {
            if (to.equalsIgnoreCase("partner")) {
                // TODO real redirect implementation
                response.redirect("https://partner.munchapp.co/login?token=");
            }
        } else {
            response.redirect("/account");
        }
        return null;
    }

    private ModelAndView viewLogin(Request request, Response response) {
        Map<String, Object> map = new HashMap<>();
        map.put("callbackUrl", formClient.getCallbackUrl());
        return new ModelAndView(map, "login.hbs");
    }

    private ModelAndView viewCreate(Request request, Response response) {
        Map<String, Object> map = new HashMap<>();
        return new ModelAndView(map, "create.hbs");
    }

    private Void create(Request request, Response response) {
        response.redirect(""); // TODO create and redirect
        return null;
    }

    private static List<CommonProfile> getProfiles(final Request request, final Response response) {
        SparkWebContext context = new SparkWebContext(request, response);
        ProfileManager<CommonProfile> manager = new ProfileManager<>(context);
        return manager.getAll(true);
    }

    @SuppressWarnings("unchecked")
    private static ModelAndView forceLogin(final Config config, final Request request, final Response response) {
        SparkWebContext context = new SparkWebContext(request, response);
        String clientName = context.getRequestParameter(Clients.DEFAULT_CLIENT_NAME_PARAMETER);
        Client client = config.getClients().findClient(clientName);
        HttpAction action;
        try {
            action = client.redirect(context);
        } catch (final HttpAction e) {
            action = e;
        }
        config.getHttpActionAdapter().adapt(action.getCode(), context);
        return null;
    }
}
