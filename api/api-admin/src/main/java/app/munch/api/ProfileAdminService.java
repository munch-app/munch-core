package app.munch.api;

import app.munch.manager.ArticleEntityManager;
import app.munch.manager.ArticlePlaceEntityManager;
import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 2019-08-13
 * Time: 21:51
 */
public final class ProfileAdminService extends AdminService {

    private final ArticleEntityManager articleEntityManager;
    private final ArticlePlaceEntityManager articlePlaceEntityManager;

    @Inject
    ProfileAdminService(TransactionProvider provider, ArticleEntityManager articleEntityManager, ArticlePlaceEntityManager articlePlaceEntityManager) {
        super(provider);
        this.articleEntityManager = articleEntityManager;
        this.articlePlaceEntityManager = articlePlaceEntityManager;
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
                            POST("/publish", this::profileArticleRevisionPublish);
                            GET("/:uid", this::profileArticleRevisionGet);
                        });
                    });
                });
            });
        });
    }

    public TransportList profileList(TransportContext ctx) {
        int size = ctx.querySize(100, 100);
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
                builder.put("uid", article.getUid());
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
            Profile profile = entityManager.find(Profile.class, profileId);
            HibernateUtils.initialize(profile.getLinks());
            return profile;
        });
    }

    public Profile profilePatch(TransportContext ctx) {
        String profileId = ctx.pathString("profileId");
        JsonNode body = ctx.bodyAsJson();

        return provider.reduce(entityManager -> {
            Profile profile = entityManager.find(Profile.class, profileId);

            return EntityPatch.with(entityManager, profile, body)
                    .lock()
                    .patch("username", Profile::setUsername)
                    .patch("name", Profile::setName)
                    .patch("bio", Profile::setBio)
                    .patch("image", (EntityPatch.NodeConsumer<Profile>) (entity, json) -> {
                        Image.EntityUtils.map(entityManager, json, profile::setImage);
                    })
                    .persist();
        });
    }

    public TransportList profileArticleList(TransportContext ctx) {
        final int size = ctx.querySize(100, 100);
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

        Article article = articleEntityManager.patch(articleId, body, null);
        if (article.getStatus() == ArticleStatus.DELETED) {
            articlePlaceEntityManager.deleteAll(articleId);
        }
        return article;
    }

    public TransportResult profileArticleRevisionPost(TransportContext ctx) {
        String articleId = ctx.pathString("articleId");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(false);

        revision = articleEntityManager.post(articleId, revision, null);
        return TransportResult.ok(Map.of("uid", revision.getUid()));
    }

    public TransportResult profileArticleRevisionPublish(TransportContext ctx) {
        String articleId = ctx.pathString("articleId");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(true);

        revision = articleEntityManager.post(articleId, revision, null);
        List<ArticlePlaceEntityManager.Response> responses = articlePlaceEntityManager.populateAll(Profile.ADMIN_ID, revision);

        return TransportResult.ok(Map.of(
                "revision", revision,
                "responses", responses
        ));
    }

    public ArticleRevision profileArticleRevisionGet(TransportContext ctx) {
        String articleId = ctx.pathString("articleId");
        String uid = ctx.pathString("uid");

        return articleEntityManager.get(articleId, uid, null);
    }
}
