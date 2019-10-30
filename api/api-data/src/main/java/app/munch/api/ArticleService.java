package app.munch.api;

import app.munch.controller.ArticleController;
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
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class ArticleService extends ApiService {

    private final ArticleQuery articleQuery;
    private final ArticleQuery.ImageQuery imageQuery;

    private final ArticleController articleController;
    private final RestrictionController restrictionController;

    @Inject
    ArticleService(ArticleQuery articleQuery, ArticleQuery.ImageQuery imageQuery, ArticleController articleController, RestrictionController restrictionController) {
        this.articleQuery = articleQuery;
        this.imageQuery = imageQuery;
        this.articleController = articleController;
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

    /**
     * Logged in profile is creating a new Article
     */
    public Article meArticlePost(TransportContext ctx) {
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);

        return articleController.post(revision, entityManager -> {
            Profile profile = findProfile(entityManager, ctx);

            // Checking user has write access
            RestrictionException.check(entityManager, profile, ProfileRestrictionType.ARTICLE_WRITE);
            return profile;
        });
    }

    /**
     * Logged in profile is getting an Article they own
     */
    public Article meArticleGet(TransportContext ctx) {
        return getAuthorized(ctx, "id", Article.class, ArticleModel::getProfile);
    }

    public TransportList<ArticleQuery.ImageQuery.ImageGroup> meArticleImagesQuery(TransportContext ctx) {
        String id = ctx.pathString("id");

        return provider.reduce(entityManager -> {
            Article article = entityManager.find(Article.class, id);
            authorized(entityManager, ctx, article.getProfile());

            return imageQuery.query(entityManager, article, ctx.queryCursor());
        });
    }

    public Article meArticlePatch(TransportContext ctx) {
        String articleId = ctx.pathString("id");
        JsonNode body = ctx.bodyAsJson();

        return articleController.patch(articleId, body, (entityManager, articleObj) -> {
            // Checking user has write access
            authorized(entityManager, ctx, articleObj.getProfile());
            restrictionController.check(entityManager, articleObj.getProfile(), ProfileRestrictionType.ARTICLE_WRITE);
        });
    }

    public ArticleController.PostResponse meArticleRevisionPost(TransportContext ctx) {
        String id = ctx.pathString("id");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(false);

        return articleController.post(id, revision, (entityManager, article) -> {
            authorized(entityManager, ctx, article.getProfile());
        });
    }

    public ArticleController.PostResponse meArticleRevisionPublish(TransportContext ctx) {
        String id = ctx.pathString("id");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(true);

        return articleController.post(id, revision, (entityManager, article) -> {
            authorized(entityManager, ctx, article.getProfile());
        });
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
                    MediaQuery.query(entityManager, cursor, b -> {
                        b.where("profile", article.getProfile());
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
