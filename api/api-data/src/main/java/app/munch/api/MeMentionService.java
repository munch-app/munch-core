package app.munch.api;

import app.munch.model.*;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.err.ValidationException;
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
@Deprecated
public final class MeMentionService extends ApiService {

    /**
     * MeMentionService is not yet used by any interface yet.
     */
    @Override
    public void route() {
        PATH("/me/mentions", () -> {
            GET("", this::query);
            POST("", this::post);

            PATH("/:id", () -> {
                GET("", this::idGet);
                PATCH("", this::idPatch);
            });
        });
    }

    public TransportList query(TransportContext ctx) {
        final int size = ctx.querySize(20, 40);

        return queryAuthorized(ctx, "FROM Mention", Mention.class, (profile, cursor, chain) -> {
            chain.orderBy("createdAt DESC");
            chain.orderBy("id DESC");
            chain.size(size);

            chain.where("profile = :profile", "profile", profile);

            if (cursor.has("createdAt", "id")) {
                chain.where("(createdAt < :createdAt OR (createdAt = :createdAt AND id < :id))",
                        "id", cursor.get("id"), "createdAt", cursor.getDate("createdAt")
                );
            }

            if (cursor.has("statuses")) {
                chain.where("status IN (:statuses)", "statuses", cursor.getEnums("statuses", MentionStatus.class));
            }

            if (cursor.has("types")) {
                chain.where("type IN (:types)", "types", cursor.getEnums("statuses", MentionType.class));
            }
        }, (builder, mention) -> {
            builder.put("createdAt", mention.getCreatedAt().getTime());
            builder.put("id", mention.getId());
        });
    }

    /**
     * @deprecated migrate to use MentionController
     */
    @Deprecated
    public Mention post(TransportContext ctx) {
        Mention mention = ctx.bodyAsObject(Mention.class);

        return postAuthorized(ctx, (entityManager, profile) -> {
            switch (mention.getStatus()) {
                case UNKNOWN_TO_SDK_VERSION:
                case SUGGESTED:
                    throw new ValidationException("status", "['PUBLIC','DELETED'] Only");
            }

            switch (mention.getType()) {
                case ARTICLE:
                    Article article = entityManager.find(Article.class, mention.getArticle().getId());
                    if (article == null) throw new NotFoundException("Article not found.");
                    mention.setArticle(article);
                    Profile.authorize(article.getProfile(), profile);
                    break;

                case MEDIA:
                    ProfileMedia media = entityManager.find(ProfileMedia.class, mention.getMedia().getId());
                    if (media == null) throw new NotFoundException("ProfileMedia not found.");
                    mention.setMedia(media);
                    Profile.authorize(media.getProfile(), profile);
                    break;
            }

            mention.setProfile(profile);
            mention.setCreatedBy(profile);

            entityManager.persist(mention);
            return mention;
        });
    }

    public Mention idGet(TransportContext ctx) {
        final String id = ctx.pathString("id");
        return getAuthorized(ctx, entityManager -> {
            return entityManager.find(Mention.class, id);
        }, Mention::getProfile);
    }

    public Mention idPatch(TransportContext ctx) {
        final String id = ctx.pathString("id");
        return patchAuthorized(ctx, entityManager -> {
            return entityManager.find(Mention.class, id);
        }, Mention::getProfile, patcher -> {
            return patcher.patch("status", MentionStatus.class, Mention::setStatus)
                    .persist();
        });
    }
}
