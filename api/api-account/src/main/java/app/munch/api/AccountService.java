package app.munch.api;

import app.munch.err.AuthenticateException;
import app.munch.model.Account;
import app.munch.model.Profile;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;
import munch.notification.MunchMailingListClient;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.firebase.FirebaseAuthenticator;
import munch.restful.server.jwt.AuthenticatedToken;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 12:22 pm
 */
public final class AccountService implements TransportService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final TransactionProvider provider;
    private final FirebaseAuthenticator authenticator;

    private final MunchMailingListClient munchMailingListClient;

    @Inject
    AccountService(TransactionProvider provider, FirebaseAuthenticator authenticator, MunchMailingListClient munchMailingListClient) {
        this.provider = provider;
        this.authenticator = authenticator;
        this.munchMailingListClient = munchMailingListClient;
    }

    @Override
    public void route() {
        PATH("/accounts", () -> {
            PATH("/tokens", () -> {
                POST("/authenticate", this::authenticate);
                GET("/custom", this::custom);
            });

            POST("/setup", this::setup);
        });
    }

    public Account authenticate(TransportContext ctx) throws AuthenticateException {
        ApiRequest request = ctx.get(ApiRequest.class);
        AuthenticatedToken token = request.getToken();
        String accountId = request.getAccountId();

        Objects.requireNonNull(token);
        FirebaseToken firebaseToken = authenticator.authenticate(token.getDecodedJWT())
                .getFirebaseToken();

        return provider.reduce(entityManager -> {
            Account account = entityManager.find(Account.class, accountId);

            if (account == null) {
                account = newAccount(accountId, firebaseToken.getEmail(), firebaseToken.getName());
                postMailing(account);
            }

            account.setAuthenticatedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(account);
            Hibernate.initialize(account);
            return account;
        });
    }

    public Account setup(TransportContext ctx) throws ForbiddenException {
        JsonNode body = ctx.bodyAsJson();
        String accountId = ctx.get(ApiRequest.class).getAccountId();

        return provider.reduce(entityManager -> {
            Account account = entityManager.find(Account.class, accountId);
            if (account != null) {
                throw new ForbiddenException("Account already setup.");
            }

            account = newAccount(accountId, body.path("email").asText(), body.path("name").asText());
            postMailing(account);

            account.setAuthenticatedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(account);
            Hibernate.initialize(account);
            return account;
        });
    }

    public TransportResult custom(TransportContext ctx) throws ExecutionException, InterruptedException {
        String accountId = ctx.get(ApiRequest.class).getAccountId();

        String token = FirebaseAuth.getInstance()
                .createCustomTokenAsync(accountId)
                .get();

        return result(builder -> {
            builder.data(Map.of("token", token));
        });
    }

    /**
     * In-house this service
     *
     * @deprecated TODO(fuxing): migrating of MunchMailingListClient required
     */
    @Deprecated
    public void postMailing(Account account) {
        try {
            munchMailingListClient.post(account.getEmail(), account.getProfile().getName());
        } catch (StructuredException e) {
            logger.warn("MallingList Error accountId: {}, email: {}", account.getId(), account.getEmail(), e);
        }
    }

    private static Account newAccount(String accountId, String email, String name) throws AuthenticateException {
        if (StringUtils.isAnyBlank(email, name)) {
            throw new AuthenticateException();
        }
        if (StringUtils.length(name) > 100) {
            throw new AuthenticateException();
        }
        if (StringUtils.length(email) > 320) {
            throw new AuthenticateException();
        }

        Account account = new Account();
        account.setId(accountId);
        account.setEmail(email);

        Profile profile = new Profile();
        profile.setName(name);
        account.setProfile(profile);
        return account;
    }
}
