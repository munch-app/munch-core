package app.munch.api;

import app.munch.model.Account;
import app.munch.model.Image;
import app.munch.model.Profile;
import app.munch.username.UsernameValidator;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ConflictException;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
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

            POST("/profile", this::post);
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

        return provider.reduce(entityManager -> {
            Account account = entityManager.find(Account.class, id);
            return EntityPatch.with(entityManager, account, body)
                    .lock()
                    .patch("email", Account::setEmail)
                    .patch("profile", Account::getProfile, patcher -> {
                        patcher.patch("name", Profile::setName);
                        patcher.patch("bio", Profile::setBio);
                        patcher.patch("image", (EntityPatch.NodeConsumer<Profile>) (profile, json) -> {
                            String imageId = json.path("id").asText(null);
                            if (imageId != null) {
                                profile.setImage(entityManager.find(Image.class, imageId));
                            } else {
                                profile.setImage(null);
                            }
                        });
                    })
                    .persist();
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

    public Profile post(TransportContext ctx) {
        ApiRequest request = ctx.get(ApiRequest.class);
        @NotNull String id = request.getAccountId();
        JsonNode body = ctx.bodyAsJson();
        String username = body.path("username").asText();

        return provider.reduce(entityManager -> {
            if (!usernameValidator.isValid(entityManager, username)) {
                throw new UnauthorizedException("Username not available.");
            }

            Account account = entityManager.find(Account.class, id);
            if (account.getProfile() != null) {
                throw new ConflictException("Profile already exist.");
            }

            Profile profile = new Profile();
            profile.setUsername(body.path("username").asText());
            profile.setName(body.path("name").asText());
            profile.setBio(body.path("bio").asText());

            String imageId = body.path("image").path("id").asText(null);
            if (imageId != null) {
                profile.setImage(entityManager.find(Image.class, imageId));
            }

            account.setProfile(profile);
            entityManager.persist(account);
            return profile;
        });

    }
}
