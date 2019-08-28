package app.munch.api;

import app.munch.model.Account;
import app.munch.model.Image;
import app.munch.model.Profile;
import app.munch.model.ProfileLink;
import app.munch.username.UsernameValidator;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
public final class MeService extends DataService {

    private final UsernameValidator usernameValidator;

    @Inject
    MeService(TransactionProvider provider, UsernameValidator usernameValidator) {
        super(provider);
        this.usernameValidator = usernameValidator;
    }

    @Override
    public void route() {
        PATH("/me", () -> {
            GET("", this::get);
            PATCH("", this::patch);

            POST("/profile/username/attempt", this::usernameAttempt);
        });
    }

    public Account get(TransportContext ctx) {
        ApiRequest request = ctx.get(ApiRequest.class);
        @NotNull String id = request.getAccountId();

        return provider.reduce(true, entityManager -> {
            return entityManager.find(Account.class, id);
        });
    }

    public Account patch(TransportContext ctx) {
        ApiRequest request = ctx.get(ApiRequest.class);
        @NotNull String id = request.getAccountId();
        JsonNode body = ctx.bodyAsJson();

        JsonNode usernameNode = body.path("profile").path("username");

        return provider.reduce(entityManager -> {
            Account account = entityManager.find(Account.class, id);

            if (usernameNode.isValueNode()) {
                String username = usernameNode.asText();
                if (!account.getProfile().getUsername().equals(username)) {
                    if (!usernameValidator.isValid(entityManager, username)) {
                        throw new UnauthorizedException("Username not available.");
                    }
                }
            }

            account = EntityPatch.with(entityManager, account, body)
                    .lock()
                    .patch("email", Account::setEmail)
                    .patch("profile", Account::getProfile, patcher -> {
                        patcher.patch("username", Profile::setUsername);
                        patcher.patch("name", Profile::setName);
                        patcher.patch("bio", Profile::setBio);
                        patcher.patch("image", (EntityPatch.NodeConsumer<Profile>) (profile, json) -> {
                            Image.EntityUtils.map(entityManager, json, profile::setImage);
                        });
                        patcher.patch("links", (EntityPatch.NodeConsumer<Profile>) (profile, json) -> {
                            List<ProfileLink> links = profile.getLinks();

                            for (JsonNode linkNode : json) {
                                // TODO(fuxing): Decide whether to delete
                            }
                        });
                    })
                    .persist();
            HibernateUtils.initialize(account.getProfile());
            return account;
        });
    }

    public Map usernameAttempt(TransportContext ctx) {
        JsonNode body = ctx.bodyAsJson();
        String username = body.path("username").asText();

        return provider.reduce(true, entityManager -> {
            return Map.of(
                    "valid", usernameValidator.isValid(entityManager, username)
            );
        });
    }
}
