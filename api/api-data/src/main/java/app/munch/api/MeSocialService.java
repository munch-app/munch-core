package app.munch.api;

import app.munch.controller.ProfileSocialController;
import app.munch.controller.QueryChain;
import app.munch.model.Profile;
import app.munch.model.ProfileSocial;
import dev.fuxing.err.ConflictException;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 28/9/19
 * Time: 10:48 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class MeSocialService extends DataService {

    private final ProfileSocialController socialController;

    @Inject
    MeSocialService(ProfileSocialController socialController) {
        this.socialController = socialController;
    }

    @Override
    public void route() {
        PATH("/me/socials", () -> {
            GET("", this::list);
            POST("/authenticate", this::authenticate);

            PATH("/:uid", () -> {
                GET("", this::uidGet);
            });
        });
    }

    public TransportList list(TransportContext ctx) {
        final String accountId = ctx.get(ApiRequest.class).getAccountId();

        return provider.reduce(true, entityManager -> {
            Profile profile = Profile.findByAccountId(entityManager, accountId);
            if (profile == null) throw new ConflictException("Profile not found.");

            return QueryChain.select(entityManager, "FROM ProfileSocial", ProfileSocial.class)
                    .where("profile = :profile", "profile", profile)
                    .orderBy("uid DESC")
                    .size(ctx.querySize(30, 50))
                    .cursor(ctx.queryCursor(), c -> c.has("uid"), (chain, cursor) -> {
                        chain.where("uid < :uid", "uid", cursor.get("uid"));
                    })
                    .asTransportList((social, builder) -> {
                        builder.put("uid", social.getUid());
                    });
        });
    }

    public TransportResult authenticate(TransportContext ctx) {
        final String accountId = ctx.get(ApiRequest.class).getAccountId();

        ProfileSocial social = ctx.bodyAsObject(ProfileSocial.class);
        socialController.authenticate(social, entityManager -> {
            return Profile.findByAccountId(entityManager, accountId);
        });
        return TransportResult.ok();
    }

    public ProfileSocial uidGet(TransportContext ctx) {
        final String uid = ctx.pathString("uid");
        final String accountId = ctx.get(ApiRequest.class).getAccountId();

        return provider.reduce(true, entityManager -> {
            ProfileSocial social = entityManager.find(ProfileSocial.class, uid);
            if (social == null) return null;

            Profile.authorize(entityManager, accountId, social.getProfile());
            return social;
        });
    }
}
