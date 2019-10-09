package app.munch.api;

import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Singleton;

/**
 * Date: 28/9/19
 * Time: 10:48 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class MeMediaService extends ApiService {

    @Override
    public void route() {
        PATH("/me/medias", () -> {
            GET("", this::query);

            PATH("/:id", () -> {
                GET("", this::idGet);
                PATCH("", this::idPatch);
            });
        });
    }

    public TransportList query(TransportContext ctx) {
        final int size = ctx.querySize(20, 50);

        return queryAuthorized(ctx, "FROM ProfileMedia", ProfileMedia.class, (profile, cursor, chain) -> {
            chain.size(size);
            chain.where("profile = :profile", "profile", profile);

            if (cursor.has("status")) {
                chain.where("status = :status", "status", cursor.getEnum("status", ProfileMediaStatus.class));
            }

            if (cursor.has("createdAt", "id")) {
                chain.where("(createdAt < :createdAt OR (createdAt = :createdAt AND id < :id)",
                        "createdAt", cursor.getDate("createdAt"), "id", cursor.get("id")
                );
            }

            chain.orderBy("createdAt DESC, id DESC");
        }, (builder, media) -> {
            builder.put("id", media.getId());
            builder.put("createdAt", media.getCreatedAt().getTime());
        });
    }

    public ProfileMedia idGet(TransportContext ctx) {
        final String id = ctx.pathString("id");

        return getAuthorized(ctx, entityManager -> {
            ProfileMedia media = entityManager.find(ProfileMedia.class, id);
            HibernateUtils.initialize(media.getProfile());
            HibernateUtils.initialize(media.getImages());
            return media;
        }, ProfileMedia::getProfile);
    }

    public ProfileMedia idPatch(TransportContext ctx) {
        final String id = ctx.pathString("id");

        return patchAuthorized(ctx, entityManager -> {
            ProfileMedia media = entityManager.find(ProfileMedia.class, id);
            HibernateUtils.initialize(media.getProfile());
            HibernateUtils.initialize(media.getImages());
            return media;
        }, ProfileMedia::getProfile, patcher -> {
            patcher.patch("status", ProfileMediaStatus.class, ProfileMedia::setStatus);
            return patcher.persist();
        });
    }
}
