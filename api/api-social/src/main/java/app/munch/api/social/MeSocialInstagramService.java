package app.munch.api.social;

import app.munch.api.ApiRequest;
import app.munch.model.*;
import com.instagram.InstagramApi;
import com.instagram.model.AccessTokenResponse;
import dev.fuxing.err.ConflictException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

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

    public TransportResult authenticate(TransportContext ctx) throws IOException {
        final String accountId = ctx.get(ApiRequest.class).getAccountId();
        final String code = ctx.bodyAsJson().path("code").asText();
        final String redirectUri = ctx.bodyAsJson().path("redirectUri").asText();

        AccessTokenResponse response = api.postAccessToken(code, redirectUri);
        String eid = response.getUser().getId();

        provider.with(entityManager -> {
            Profile profile = Profile.findByAccountId(entityManager, accountId);
            if (profile == null) throw new ConflictException("Profile not found.");

            ProfileSocial social = findByTypeEid(entityManager, ProfileSocialType.INSTAGRAM, eid);
            if (social != null) {
                if (!social.getProfile().getUid().equals(profile.getUid())) {
                    throw new ConflictException("This account is owned by another profile.");
                }
            } else {
                social = new ProfileSocial();
                social.setEid(eid);
                social.setType(ProfileSocialType.INSTAGRAM);
                social.setProfile(profile);
            }

            social.setName(response.getUser().getUsername());
            social.setStatus(ProfileSocialStatus.CONNECTED);
            entityManager.persist(social);

            InstagramAccountConnection connection = findConnection(entityManager, social);
            connection.setStatus(InstagramAccountConnectionStatus.CONNECTED);
            connection.setAccessToken(response.getAccessToken());
            connection.setSocial(social);
            entityManager.persist(connection);

        });
        return TransportResult.ok();
    }

    private InstagramAccountConnection findConnection(EntityManager entityManager, ProfileSocial social) {
        List<InstagramAccountConnection> list = entityManager.createQuery("FROM InstagramAccountConnection WHERE social = :social", InstagramAccountConnection.class)
                .setParameter("social", social)
                .setMaxResults(1)
                .getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return new InstagramAccountConnection();
    }
}
