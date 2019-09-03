package app.munch.api;

import app.munch.exception.RestrictionException;
import app.munch.manager.ArticleEntityManager;
import app.munch.manager.ArticlePlaceEntityManager;
import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.jpa.TransactionProvider;
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

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class ArticleService extends DataService {

    private final ArticleEntityManager articleEntityManager;
    private final ArticlePlaceEntityManager articlePlaceEntityManager;

    @Inject
    ArticleService(TransactionProvider provider, ArticleEntityManager articleEntityManager, ArticlePlaceEntityManager articlePlaceEntityManager) {
        super(provider);
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
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        final ArticleStatus status = ctx.queryEnum("status", ArticleStatus.class);

        TransportCursor cursor = ctx.queryCursor();
        int size = ctx.querySize(20, 50);

        return articleEntityManager.list(status, entityManager -> {
            return entityManager.createQuery("SELECT a.profile FROM Account a " +
                    "WHERE a.id = :id", Profile.class)
                    .setParameter("id", accountId)
                    .getSingleResult();
        }, size, cursor);
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

        return articleEntityManager.get(id, (entityManager, article) -> {
            validate(entityManager, article, ctx);
        });
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
        return TransportResult.ok(Map.of("uid", revision.getUid()));
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

        return TransportResult.ok(Map.of(
                "revision", revision,
                "responses", responses
        ));
    }

    public ArticleRevision meArticleRevisionGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        String uid = ctx.pathString("uid");

        return articleEntityManager.get(id, uid, (entityManager, articleRevision) -> {
            validate(entityManager, articleRevision.getArticle(), ctx);
        });
    }

    public Article articleGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        return articleEntityManager.get(id, (entityManager, article) -> {
            if (article.getStatus() != ArticleStatus.PUBLISHED) {
                throw new ForbiddenException();
            }
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
