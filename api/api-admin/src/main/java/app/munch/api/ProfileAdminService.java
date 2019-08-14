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
                            GET("/:revision", this::profileArticleRevisionGet);
                        });
                    });
                });
            });
        });
    }

    public TransportList profileList(TransportContext ctx) {
        int size = ctx.querySize(20, 50);
        @NotNull TransportCursor cursor = ctx.queryCursor();
        String cursorId = cursor.get("id");

        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (cursorId != null) {
                    return entityManager.createQuery("FROM Profile " +
                            "WHERE id < :cursorId " +
                            "ORDER BY id DESC", Profile.class)
                            .setParameter("cursorId", cursorId)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Profile " +
                        "ORDER BY id DESC", Profile.class)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (article, builder) -> {
                builder.put("id", article.getId());
            }).asTransportList();
        });
    }

    public Profile profilePost(TransportContext ctx) {
        Profile profile = ctx.bodyAsObject(Profile.class);

        return provider.reduce(entityManager -> {
            if (profile.getImage() != null) {
                profile.setImage(entityManager.find(Image.class, profile.getImage().getId()));
            }

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
        String profileId = ctx.pathString("profileId");
        TransportCursor cursor = ctx.queryCursor();

        return articleEntityManager.list(entityManager -> {
            return entityManager.find(Profile.class, profileId);
        }, size, cursor);

    }

    public Article profileArticlePost(TransportContext ctx) {
        String profileId = ctx.pathString("profileId");
        JsonNode body = ctx.bodyAsJson();

        return articleEntityManager.post(entityManager -> {
            return entityManager.find(Profile.class, profileId);
        }, body);
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
        String revision = ctx.pathString("revision");

        return articleEntityManager.getRevision(articleId, revision, null);
    }
}
