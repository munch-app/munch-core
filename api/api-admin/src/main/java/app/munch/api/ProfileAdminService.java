package app.munch.api;

import app.munch.manager.ArticleEntityManager;
import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Created by: Fuxing
 * Date: 2019-08-13
 * Time: 21:51
 */
public final class ProfileAdminService extends AdminService {

    private final ArticleEntityManager articleEntityManager;

    @Inject
    ProfileAdminService(TransactionProvider provider, ArticleEntityManager articleEntityManager) {
        super(provider);
        this.articleEntityManager = articleEntityManager;
    }

    @Override
    public void route() {
        PATH("/admin/profiles", () -> {
            GET("", this::profileList);
            POST("", this::profilePost);

            PATH("/:profileId", () -> {
                GET("", this::profileGet);
                PATCH("", this::profilePatch);


                PATH("/articles", () -> {
                    GET("", this::profileArticleList);
                    POST("", this::profileArticlePost);

                    PATH("/:articleId", () -> {
                        GET("", this::profileArticleGet);
                        PATCH("", this::profileArticlePatch);

                        PATH("/revisions", () -> {
                            POST("", this::profileArticleRevisionPost);
                            GET("/:uid", this::profileArticleRevisionGet);
                        });
                    });
                });
            });
        });
    }

    public TransportList profileList(TransportContext ctx) {
        int size = ctx.querySize(20, 50);
        @NotNull TransportCursor cursor = ctx.queryCursor();
        String uid = cursor.get("uid");

        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (uid != null) {
                    return entityManager.createQuery("FROM Profile " +
                            "WHERE uid < :uid " +
                            "ORDER BY uid DESC", Profile.class)
                            .setParameter("uid", uid)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Profile " +
                        "ORDER BY uid DESC", Profile.class)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (article, builder) -> {
                builder.put("id", article.getUid());
            }).asTransportList();
        });
    }

    public Profile profilePost(TransportContext ctx) {
        Profile profile = ctx.bodyAsObject(Profile.class);

        return provider.reduce(entityManager -> {
            Image.EntityUtils.map(entityManager, profile.getImage(), profile::setImage);

            entityManager.persist(profile);
            return profile;
        });
    }

    public Profile profileGet(TransportContext ctx) {
        String profileId = ctx.pathString("profileId");

        return provider.reduce(entityManager -> {
            return entityManager.find(Profile.class, profileId);
        });
    }

    public Profile profilePatch(TransportContext ctx) {
        String profileId = ctx.pathString("profileId");
        JsonNode body = ctx.bodyAsJson();

        return provider.reduce(entityManager -> {
            Profile profile = entityManager.find(Profile.class, profileId);

            return EntityPatch.with(entityManager, profile, body)
                    .lock()
                    .patch("name", Profile::setName)
                    .patch("bio", Profile::setBio)
                    .patch("image", (EntityPatch.NodeConsumer<Profile>) (entity, json) -> {
                        String imageId = json.path("id").asText(null);
                        if (imageId != null) {
                            entity.setImage(entityManager.find(Image.class, imageId));
                        } else {
                            entity.setImage(null);
                        }
                    })
                    .persist();
        });
    }

    public TransportList profileArticleList(TransportContext ctx) {
        final int size = ctx.querySize(20, 50);
        final ArticleStatus status = ctx.queryEnum("status", ArticleStatus.class);

        String profileId = ctx.pathString("profileId");
        TransportCursor cursor = ctx.queryCursor();

        return articleEntityManager.list(status, entityManager -> {
            return entityManager.find(Profile.class, profileId);
        }, size, cursor);
    }

    public Article profileArticlePost(TransportContext ctx) {
        String profileId = ctx.pathString("profileId");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);

        return articleEntityManager.post(revision, entityManager -> {
            return entityManager.find(Profile.class, profileId);
        });
    }

    public Article profileArticleGet(TransportContext ctx) {
        String articleId = ctx.pathString("articleId");
        return articleEntityManager.get(articleId, null);
    }

    public Article profileArticlePatch(TransportContext ctx) {
        String articleId = ctx.pathString("articleId");
        JsonNode body = ctx.bodyAsJson();

        return articleEntityManager.patch(articleId, body, null);
    }

    public ArticleRevision profileArticleRevisionPost(TransportContext ctx) {
        String articleId = ctx.pathString("articleId");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);

        return articleEntityManager.post(articleId, revision, null);
    }

    public ArticleRevision profileArticleRevisionGet(TransportContext ctx) {
        String articleId = ctx.pathString("articleId");
        String uid = ctx.pathString("uid");

        return articleEntityManager.getRevision(articleId, uid, null);
    }
}
