package app.munch.api.social;

import app.munch.api.ApiRequest;
import app.munch.model.Profile;
import app.munch.model.ProfileSocial;
import app.munch.model.ProfileSocialStatus;
import app.munch.model.ProfileSocialType;
import com.instagram.model.AccessTokenResponse;
import com.instagram.InstagramApi;
import dev.fuxing.err.ConflictException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;

/**
 * Date: 2/10/19
 * Time: 1:22 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class MeSocialInstagramService implements SocialService {

    private final TransactionProvider provider;
    private final InstagramApi api;

    @Inject
    public MeSocialInstagramService(TransactionProvider provider, InstagramApi api) {
        this.provider = provider;
        this.api = api;
    }

    @Override
    public void route() {
        PATH("/me/socials/instagram", () -> {
            POST("/authenticate", this::authenticate);
        });
    }

    public TransportResult authenticate(TransportContext ctx) throws IOException, URISyntaxException {
        final String accountId = ctx.get(ApiRequest.class).getAccountId();
        final String code = ctx.bodyAsJson().path("code").asText();
        final String redirectUri = ctx.bodyAsJson().path("redirectUri").asText();

        AccessTokenResponse response = api.postAccessToken(code, redirectUri);
        String eid = response.getUser().getId();

        provider.with(entityManager -> {
            Profile profile = Profile.findByAccountId(entityManager, accountId);
            if (profile == null) throw new ConflictException("Profile not found.");

            ProfileSocial entity = findByTypeEid(entityManager, ProfileSocialType.INSTAGRAM, eid);
            if (entity != null) {
                if (!entity.getProfile().getUid().equals(profile.getUid())) {
                    throw new ConflictException("This account is owned by another profile.");
                }
            } else {
                entity = new ProfileSocial();
                entity.setEid(eid);
                entity.setType(ProfileSocialType.INSTAGRAM);
                entity.setProfile(profile);
            }

            entity.setName(response.getUser().getUsername());
            entity.setStatus(ProfileSocialStatus.CONNECTED);
            entity.setSecrets(createSecrets(response.getAccessToken()));
            entity.setConnectedAt(new Timestamp(System.currentTimeMillis()));

            entityManager.persist(entity);
        });
        return TransportResult.ok();
    }

    private static ProfileSocial.Secrets createSecrets(String accessToken) {
        ProfileSocial.Secrets secrets = new ProfileSocial.Secrets();
        secrets.setAccessToken(accessToken);
        return secrets;
    }
}
