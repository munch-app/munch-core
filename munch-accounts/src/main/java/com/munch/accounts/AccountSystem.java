package com.munch.accounts;

import com.munch.accounts.controller.PacConfigFactory;
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
import spark.TemplateEngine;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 1:58 AM
 * Project: munch-core
 */
public class AccountSystem {

    private static final TemplateEngine templateEngine = new HandlebarsTemplateEngine();

    public static void main(String[] args) {
        port(8080);

        get("/", AccountSystem::index, templateEngine);

        final Config config = new PacConfigFactory(templateEngine).build();
        final CallbackRoute callback = new CallbackRoute(config, null, true);
        get("/callback", callback);
        post("/callback", callback);

        final SecurityFilter facebookFilter = new SecurityFilter(config, "FacebookClient", "", "excludedPath");
        before("/facebook", facebookFilter);
        before("/facebook/*", facebookFilter);
        before("/form", new SecurityFilter(config, "FormClient"));

        get("/facebook", AccountSystem::protectedIndex, templateEngine);
        get("/loginForm", (rq, rs) -> form(config), templateEngine);

        get("/form", AccountSystem::protectedIndex, templateEngine);

        get("/logout", new ApplicationLogoutRoute(config, "/"));
    }

    private static ModelAndView index(final Request request, final Response response) {
        Map<String, Object> map = new HashMap<>();
        map.put("profiles", getProfiles(request, response));
        return new ModelAndView(map, "index.mustache");
    }

    private static ModelAndView form(final Config config) {
        Map<String, Object> map = new HashMap<>();
        FormClient formClient = config.getClients().findClient(FormClient.class);
        map.put("callbackUrl", formClient.getCallbackUrl());
        return new ModelAndView(map, "loginForm.mustache");
    }

    private static ModelAndView protectedIndex(final Request request, final Response response) {
        Map<String, Object> map = new HashMap<>();
        map.put("profiles", getProfiles(request, response));
        return new ModelAndView(map, "protectedIndex.mustache");
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
