package app.munch.api;

import app.munch.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;
import munch.notification.MunchMailingListClient;
import munch.restful.core.exception.StructuredException;
import munch.restful.server.firebase.FirebaseAuthenticator;
import munch.restful.server.jwt.AuthenticatedToken;
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
        PATH("/accounts/tokens", () -> {
            GET("/custom", this::customToken);
            POST("/authenticate", this::authenticate);
        });
    }

    public Account authenticate(TransportContext ctx) {
        ApiRequest request = ctx.get(ApiRequest.class);
        AuthenticatedToken token = request.getToken();
        String accountId = request.getAccountId();

        Objects.requireNonNull(token);
        FirebaseToken firebaseToken = authenticator.authenticate(token.getDecodedJWT())
                .getFirebaseToken();

        return provider.reduce(entityManager -> {
            Account account = entityManager.find(Account.class, accountId);
            if (account == null) {
                account = new Account();
                account.setId(accountId);
                account.setEmail(firebaseToken.getEmail());
                account.setName(firebaseToken.getName());

                try {
                    munchMailingListClient.post(account.getEmail(), account.getName());
                } catch (StructuredException e) {
                    logger.warn("MallingList Error accountId: {}, email: {}", account.getId(), account.getEmail(), e);
                }
            }

            account.setAuthenticatedAt(new Timestamp(System.currentTimeMillis()));

            entityManager.persist(account);
            HibernateUtils.initialize(account);
            return account;
        });
    }

    public TransportResult customToken(TransportContext ctx) throws ExecutionException, InterruptedException {
        String accountId = ctx.get(ApiRequest.class).getAccountId();

        String token = FirebaseAuth.getInstance()
                .createCustomTokenAsync(accountId)
                .get();

        return result(200,
                Map.of("token", token)
        );
    }
}
