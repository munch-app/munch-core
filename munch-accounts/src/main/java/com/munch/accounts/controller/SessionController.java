package com.munch.accounts.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.accounts.objects.Account;
import com.munch.accounts.objects.Gender;
import com.munch.hibernate.utils.TransactionProvider;
import com.munch.utils.spark.SparkController;
import com.munch.utils.spark.exceptions.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.credentials.password.PasswordEncoder;
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

import java.time.Year;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by: Fuxing
 * Date: 4/2/2017
 * Time: 6:38 PM
 * Project: munch-core
 */
@Singleton
public class SessionController implements SparkController {

    private final Pattern passwordPattern;
    private final Config pacConfig;
    private final FormClient formClient;

    private final TransactionProvider provider;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public SessionController(Config pacConfig, TransactionProvider provider, PasswordEncoder passwordEncoder,
                             @Named("PasswordPattern") Pattern passwordPattern) {
        this.pacConfig = pacConfig;
        this.formClient = pacConfig.getClients().findClient(FormClient.class);
        this.provider = provider;
        this.passwordEncoder = passwordEncoder;
        this.passwordPattern = passwordPattern;
    }

    @Override
    public void route() {
        // Index page redirect
        Spark.get("/", this::index);

        // Login Callback
        CallbackRoute callback = new CallbackRoute(pacConfig, null, true);
        Spark.get("/callback", callback);
        Spark.post("/callback", callback);
        // TODO MA-15 facebook auth on callback
        // TODO MA-24 account profile image if don't exist

        Spark.get("/create", this::viewCreate, templateEngine);
        Spark.post("/create", this::create);
        Spark.after("/create", new SecurityFilter(pacConfig, "FormClient"));


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
                // TODO MA-16
                response.redirect("https://partner.munchapp.co/login?token=");
            }
        } else {
            response.redirect("https://munchapp.co/login?token=");
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

    /**
     * @param request  spark request
     * @param response spark response
     * @return Void, as user will be redirected to auto login
     */
    private Void create(Request request, Response response) throws ParamException {
        String email = queryString(request, "username");
        String password = queryString(request, "password");

        int day = queryInt(request, "dobDay");
        int month = queryInt(request, "dobMonth");
        int year = queryInt(request, "dobYear");

        // Validate
        if (password.length() < 8 || !passwordPattern.matcher(password).matches()) {
            throwsMessage("Password must contain 8 characters, including at least 1 number and letter.");
        }
        if (year > Year.now().getValue() - 1) {
            throwsMessage("Year date of birth invalid.");
        }

        Account account = new Account();
        account.setFirstName(queryString(request, "firstName"));
        account.setLastName(queryString(request, "lastName"));
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));

        // Gender, default to male if client manipulate html
        Gender gender = queryString(request, "gender").equalsIgnoreCase("female") ?
                Gender.Female : Gender.Male;
        account.setGender(gender);

        // Date of birth as Calendar
        account.setBirthDate(new GregorianCalendar(year, month, day));

        // Delegate to /login/email
        String to = request.queryParams("to");
        if (StringUtils.isNotBlank(to)) {
            response.redirect("/login/email?to=" + to);
        } else {
            response.redirect("/login/email");
        }

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
