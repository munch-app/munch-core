package com.munch.accounts.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.accounts.objects.Account;
import com.munch.accounts.objects.Country;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.time.Year;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by: Fuxing
 * Date: 4/2/2017
 * Time: 6:38 PM
 * Project: munch-core
 */
@Singleton
public class SessionController implements SparkController {
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private final String redirectPartner;
    private final String redirectMunch;

    private final Pattern passwordPattern;
    private final Config pacConfig;
    private final FormClient formClient;

    private final TransactionProvider provider;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public SessionController(Config pacConfig, TransactionProvider provider, PasswordEncoder passwordEncoder,
                             @Named("PasswordPattern") Pattern passwordPattern, com.typesafe.config.Config config) {
        this.pacConfig = pacConfig;
        this.formClient = pacConfig.getClients().findClient(FormClient.class);
        this.provider = provider;
        this.passwordEncoder = passwordEncoder;
        this.passwordPattern = passwordPattern;

        // Init redirect urls
        this.redirectPartner = config.getString("redirect.partner");
        this.redirectMunch = config.getString("redirect.munch");
    }

    @Override
    public void route() {
        // Index page
        Spark.get("/", this::index);

        // Login Callback
        CallbackRoute callback = new CallbackRoute(pacConfig, null, true);
        Spark.get("/callback", callback);
        Spark.post("/callback", callback);
        // Spark after filter to capture profiles and redirect to redirect();
        // TODO MA-15 facebook auth on callback
        // TODO MA-24 account profile image if don't exist

        // Sign up Page
        Spark.before("/signup", this::authenticatedFilter);
        Spark.before("/signup", this::persistRedirectFilter);
        Spark.get("/signup", this::viewSignup, templateEngine);
        Spark.post("/signup", this::create);

        // Login Page/Form
        Spark.before("/login", this::authenticatedFilter);
        Spark.before("/login", this::persistRedirectFilter);
        Spark.get("/login", this::viewLogin, templateEngine);

        // Login Provider (Force Login)
        Spark.before("/login/*", this::persistRedirectFilter);
        Spark.before("/login/facebook", new SecurityFilter(pacConfig, "FacebookClient"));
        Spark.before("/login/email", new SecurityFilter(pacConfig, "FormClient"));
        Spark.get("/login/facebook", this::redirect);
        Spark.get("/login/email", this::redirect);

        Spark.get("/logout", new ApplicationLogoutRoute(pacConfig, "/"));
    }

    /**
     * Redirect to persisted chaining
     */
    private void persistRedirectFilter(Request request, Response response) {
        // Persist redirect to in browser cookie store.
        String to = request.queryParams("to");
        if (StringUtils.isNotBlank(to)) {
            response.cookie("redirectTo", to, -1, true);
        }
    }

    /**
     * Check for logged in user, auto redirect
     */
    private void authenticatedFilter(Request request, Response response) {
        SparkWebContext context = new SparkWebContext(request, response);
        ProfileManager manager = new ProfileManager(context);

        // Authenticated, just redirect
        if (manager.isAuthenticated()) {
            redirect(request, response);
            Spark.halt();
        }
    }

    /**
     * Future: now just redirect to login
     * Redirect to /account if user logged in
     * or /login if user not logged in
     *
     * @param request  request
     * @param response response
     * @return null
     */
    private Void index(Request request, Response response) {
        response.redirect("/login");
        return null;
    }

    /**
     * Read redirectTo from session and choose
     * Redirect to /account or
     * https://partner.munchapp.co/callback
     *
     * @param request  request
     * @param response response
     * @return null
     */
    private Void redirect(Request request, Response response) {
        String to = request.cookie("redirectTo");
        switch (StringUtils.trimToEmpty(to)) {
            case "partner":
                response.redirect(redirectPartner + "token");
                return null;
            case "munch":
            default:
                response.redirect(redirectMunch + "token");
                return null;
        }
    }

    /**
     * Render login view
     *
     * @param request  spark request
     * @param response spark response
     * @return login.hbs view
     */
    private ModelAndView viewLogin(Request request, Response response) {
        SparkWebContext context = new SparkWebContext(request, response);
        ProfileManager manager = new ProfileManager(context);

        // Authenticated, just redirect
        if (manager.isAuthenticated()) {
            redirect(request, response);
            Spark.halt();
            return null;
        } else {
            return singleView("callbackUrl", formClient.getCallbackUrl(), "login.hbs");
        }
    }

    /**
     * Render create view
     *
     * @param request  spark request
     * @param response spark response
     * @return create.hbs view
     */
    private ModelAndView viewSignup(Request request, Response response) {
        return view("create.hbs");
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

        // Validate password and dob
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

        // Set default required fields
        account.setCountry(Country.Singapore);

        // Gender, default to male if client manipulate html
        Gender gender = queryString(request, "gender").equalsIgnoreCase("female") ?
                Gender.Female : Gender.Male;
        account.setGender(gender);

        // Date of birth as Calendar
        account.setBirthDate(new GregorianCalendar(year, month, day));

        // Persist account
        boolean created = provider.reduce(em -> {
            // Check if account already exist
            List<String> list = em.createQuery("SELECT a.id FROM Account a " +
                    "WHERE a.email = :email", String.class)
                    .setParameter("email", email).getResultList();

            if (list.isEmpty()) {
                em.persist(account);
                return true;
            } else { // Account with that email already exist
                logger.warn("Account with email: {} already exist in database with id: {}", email, list.get(0));
                return false;
            }
        });

        if (!created) {
            // Account already created, redirect to login with error
            response.redirect("/login?email=" + email
                    + "&error=Their is an existing account with the following email. Try logging in?");
            Spark.halt();
        } else {
            try {
                // Perform local login
                SparkWebContext context = new SparkWebContext(request, response);
                CommonProfile profile = formClient.getUserProfile(formClient.getCredentials(context), context);
                // Save to profile manager
                ProfileManager<CommonProfile> manager = new ProfileManager<>(context);
                manager.save(true, profile, true);

                // Send to /login for auto redirect
                response.redirect("/login");
            } catch (final HttpAction e) {
                logger.error("HTTP action required in signup {}, signup auto login should not have error.", e.getCode());
                throwsMessage("Signup auto login error.");
            }
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
