package app.munch.api;

import app.munch.exception.RestrictionException;
import app.munch.manager.ArticleEntityManager;
import app.munch.manager.ArticlePlaceEntityManager;
import app.munch.model.*;
import app.munch.query.ArticleQuery;
import app.munch.query.MediaQuery;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
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

    private final ArticleEntityManager articleEntityManager;
    private final ArticlePlaceEntityManager articlePlaceEntityManager;

    @Inject
    ArticleService(ArticleQuery articleQuery, ArticleEntityManager articleEntityManager, ArticlePlaceEntityManager articlePlaceEntityManager) {
        this.articleQuery = articleQuery;
        this.articleEntityManager = articleEntityManager;
        this.articlePlaceEntityManager = articlePlaceEntityManager;
    }

    @Override
    public void route() {
        PATH("/me/articles", () -> {
            POST("", this::meArticlePost);
            GET("", this::meArticleList);

            PATH("/:id", () -> {
                GET("", this::meArticleGet);
                PATCH("", this::meArticlePatch);

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

    /**
     * Validate ArticleId belong to AccountId
     */
    private static void validate(EntityManager entityManager, Article article, TransportContext ctx) {
        @NotNull String accountId = ctx.get(ApiRequest.class).getAccountId();

        String profileId = entityManager.createQuery("SELECT a.profile.uid FROM Account a " +
                "WHERE a.id = :id", String.class)
                .setParameter("id", accountId)
                .getSingleResult();

        if (!article.getProfile().getUid().equals(profileId)) {
            throw new UnauthorizedException();
        }
    }

    public TransportList meArticleList(TransportContext ctx) {
        return articleQuery.query(ctx.queryCursor(), entityManager -> {
            return findProfile(entityManager, ctx);
        });
    }

    public Article meArticlePost(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);

        return articleEntityManager.post(revision, entityManager -> {
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

    public Article meArticlePatch(TransportContext ctx) {
        String articleId = ctx.pathString("id");
        JsonNode body = ctx.bodyAsJson();

        Article article = articleEntityManager.patch(articleId, body, (entityManager, articleObj) -> {
            // Checking user has write access
            RestrictionException.check(entityManager, articleObj.getProfile(), ProfileRestrictionType.ARTICLE_WRITE);
            validate(entityManager, articleObj, ctx);
        });
        if (article.getStatus() == ArticleStatus.DELETED) {
            articlePlaceEntityManager.deleteAll(articleId);
        }
        return article;
    }

    public TransportResult meArticleRevisionPost(TransportContext ctx) {
        String id = ctx.pathString("id");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(false);

        revision = articleEntityManager.post(id, revision, (entityManager, article) -> {
            validate(entityManager, article, ctx);
        });

        return TransportResult.builder()
                .data(Map.of("uid", revision.getUid()))
                .build();
    }

    public TransportResult meArticleRevisionPublish(TransportContext ctx) {
        String id = ctx.pathString("id");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);
        revision.setPublished(true);

        revision = articleEntityManager.post(id, revision, (entityManager, article) -> {
            validate(entityManager, article, ctx);
        });

        String profileUid = revision.getProfile().getUid();
        List<ArticlePlaceEntityManager.Response> responses = articlePlaceEntityManager.populateAll(profileUid, revision);

        return TransportResult.builder()
                .data(Map.of(
                        "revision", revision,
                        "responses", responses
                )).build();
    }

    public ArticleRevision meArticleRevisionGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        String uid = ctx.pathString("uid");

        return articleEntityManager.get(id, uid, (entityManager, articleRevision) -> {
            validate(entityManager, articleRevision.getArticle(), ctx);
        });
    }

    public TransportResult articleGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        Set<String> fields = ctx.queryFields();

        return provider.reduce(true, entityManager -> {
            Article article = entityManager.find(Article.class, id);
            if (article == null) return null;

            if (article.getStatus() != ArticleStatus.PUBLISHED) {
                throw new ForbiddenException();
            }

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
                        query.where("m.profile = :profile", "profile", article.getProfile());
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
            throw new ForbiddenException("latest not allowed");
        }
        return articleEntityManager.get(id, uid, null);
    }
}
