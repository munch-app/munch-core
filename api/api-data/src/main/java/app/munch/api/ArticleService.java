package app.munch.api;

import app.munch.controller.ArticleController;
import app.munch.controller.ArticlePlaceController;
import app.munch.controller.RestrictionController;
import app.munch.exception.RestrictionException;
import app.munch.model.*;
import app.munch.query.ArticleQuery;
import app.munch.query.MediaQuery;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class ArticleService extends ApiService {

    private final ArticleQuery articleQuery;

    private final ArticleController articleController;
    private final ArticlePlaceController articlePlaceController;
    private final RestrictionController restrictionController;

    @Inject
    ArticleService(ArticleQuery articleQuery, ArticleController articleController, ArticlePlaceController articlePlaceController, RestrictionController restrictionController) {
        this.articleQuery = articleQuery;
        this.articleController = articleController;
        this.articlePlaceController = articlePlaceController;
        this.restrictionController = restrictionController;
    }

    @Override
    public void route() {
        PATH("/me/articles", () -> {
            POST("", this::meArticlePost);
            GET("", this::meArticleQuery);

            PATH("/:id", () -> {
                GET("", this::meArticleGet);
                PATCH("", this::meArticlePatch);

                PATH("/images", () -> {
                    GET("", this::meArticleImagesQuery);
                });

                PATH("/revisions", () -> {
                    POST("", this::meArticleRevisionPost);
                    POST("/publish", this::meArticleRevisionPublish);
                    GET("/:uid", this::meArticleRevisionGet);
                });
            });
        });

        PATH("/articles/:id", () -> {
            GET("", this::articleGet);
            GET("/revisions/:uid", this::articleRevisionGet);
        });
    }

    public TransportList meArticleQuery(TransportContext ctx) {
        return articleQuery.query(ctx.queryCursor(), entityManager -> {
            return findProfile(entityManager, ctx);
        });
    }

    public Article meArticlePost(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);

        return articleController.post(revision, entityManager -> {
            Profile profile = entityManager.createQuery("SELECT a.profile FROM Account a " +
                    "WHERE a.id = :id", Profile.class)
                    .setParameter("id", accountId)
                    .getSingleResult();

            // Checking user has write access
            RestrictionException.check(entityManager, profile, ProfileRestrictionType.ARTICLE_WRITE);
            return profile;
        });
    }

    public Article meArticleGet(TransportContext ctx) {
        String id = ctx.pathString("id");

        return getAuthorized(ctx, em -> em.find(Article.class, id), ArticleModel::getProfile);
    }

    public TransportList<TransportList> meArticleImagesQuery(TransportContext ctx) {
        // TODO(fuxing): Read from place node in article
        return null;
    }

    public Article meArticlePatch(TransportContext ctx) {
        String articleId = ctx.pathString("id");
        JsonNode body = ctx.bodyAsJson();

        Article article = articleController.patch(articleId, body, (entityManager, articleObj) -> {
            // Checking user has write access
            authorized(entityManager, ctx, articleObj.getProfile());
            restrictionController.check(entityManager, articleObj.getProfile(), ProfileRestrictionType.ARTICLE_WRITE);
        });
        if (article.getStatus() == ArticleStatus.DELETED) {
            articlePlaceController.deleteAll(articleId);
        }
        return article;
    }

    public TransportResult meArticleRevisionPost(TransportContext ctx) {
        String id = ctx.pathString("id");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(false);

        revision = articleController.post(id, revision, (entityManager, article) -> {
            authorized(entityManager, ctx, article.getProfile());
        });

        return TransportResult.builder()
                .data(Map.of("uid", revision.getUid()))
                .build();
    }

    public TransportResult meArticleRevisionPublish(TransportContext ctx) {
        String id = ctx.pathString("id");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(true);

        revision = articleController.post(id, revision, (entityManager, article) -> {
            authorized(entityManager, ctx, article.getProfile());
        });

        String profileUid = revision.getProfile().getUid();
        List<ArticlePlaceController.Response> responses = articlePlaceController.populateAll(profileUid, revision);

        return TransportResult.builder()
                .data(Map.of(
                        "revision", revision,
                        "responses", responses
                )).build();
    }

    public ArticleRevision meArticleRevisionGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        String uid = ctx.pathString("uid");

        return articleController.find(id, uid, (entityManager, articleRevision) -> {
            authorized(entityManager, ctx, articleRevision.getProfile());
        });
    }

    public TransportResult articleGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        Set<String> fields = ctx.queryFields();

        return provider.reduce(true, entityManager -> {
            Article article = articleController.find(entityManager, id, null);

            return result(builder -> {
                builder.data(article);

                if (fields.contains("extra.profile.articles")) {
                    TransportCursor cursor = TransportCursor.size(
                            ctx.queryInt("extra.profile.articles.size", 5)
                    );
                    ArticleQuery.query(entityManager, cursor, ArticleStatus.PUBLISHED, query -> {
                        query.where("profile", article.getProfile());
                        query.where("id != :aid", "aid", article.getId());
                    }).consume((articles, c) -> {
                        builder.extra("profile.articles", articles);
                    });
                }

                if (fields.contains("extra.profile.medias")) {
                    TransportCursor cursor = TransportCursor.size(
                            ctx.queryInt("extra.profile.medias.size", 5)
                    );
                    MediaQuery.query(entityManager, cursor, query -> {
                        query.where("profile", article.getProfile());
                    }).peek(media -> {
                        media.setSocial(null);
                        media.setMetric(null);
                    }).consume((medias, c) -> {
                        builder.extra("profile.medias", medias);
                    });
                }
            });
        });
    }

    public ArticleRevision articleRevisionGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        String uid = ctx.pathString("uid");
        if (uid.equals("latest")) {
            throw new ForbiddenException("Article latest not allowed.");
        }
        return articleController.find(id, uid, null);
    }
}
