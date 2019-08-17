package app.munch.api;

import app.munch.manager.ArticleEntityManager;
import app.munch.model.Article;
import app.munch.model.ArticleRevision;
import app.munch.model.Profile;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.UnauthorizedException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class ArticleService extends DataService {

    private final ArticleEntityManager articleEntityManager;

    @Inject
    ArticleService(TransactionProvider provider, ArticleEntityManager articleEntityManager) {
        super(provider);
        this.articleEntityManager = articleEntityManager;
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
                    GET("/:revision", this::meArticleRevisionGet);
                });
            });
        });

        PATH("/articles/:id", () -> {
            GET("", this::articleGet);
            GET("/revisions/:revision", this::articleRevisionGet);
        });
    }

    /**
     * Validate ArticleId belong to AccountId
     */
    private static void validate(EntityManager entityManager, Article article, TransportContext ctx) {
        @NotNull String accountId = ctx.get(ApiRequest.class).getAccountId();

        String profileId = entityManager.createQuery("SELECT a.profile.id FROM Account a " +
                "WHERE a.id = :id", String.class)
                .setParameter("id", accountId)
                .getSingleResult();

        if (!article.getProfile().getId().equals(profileId)) {
            throw new UnauthorizedException();
        }
    }

    public TransportList meArticleList(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        TransportCursor cursor = ctx.queryCursor();
        int size = ctx.querySize(20, 50);

        return articleEntityManager.list(entityManager -> {
            return entityManager.createQuery("SELECT a.profile FROM Account a " +
                    "WHERE a.id = :id", Profile.class)
                    .setParameter("id", accountId)
                    .getSingleResult();
        }, size, cursor);
    }

    public Article meArticlePost(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        JsonNode body = ctx.bodyAsJson();

        return articleEntityManager.post(entityManager -> {
            return entityManager.createQuery("SELECT a.profile FROM Account a " +
                    "WHERE a.id = :id", Profile.class)
                    .setParameter("id", accountId)
                    .getSingleResult();
        }, body);
    }

    public Article meArticleGet(TransportContext ctx) {
        String id = ctx.pathString("id");

        return articleEntityManager.get(id, (entityManager, article) -> {
            validate(entityManager, article, ctx);
        });
    }

    public Article meArticlePatch(TransportContext ctx) {
        String id = ctx.pathString("id");
        JsonNode body = ctx.bodyAsJson();

        return articleEntityManager.patch(id, body, (entityManager, article) -> {
            validate(entityManager, article, ctx);
        });
    }

    public ArticleRevision meArticleRevisionPost(TransportContext ctx) {
        String id = ctx.pathString("id");
        ArticleRevision revision = ctx.bodyAsObject(ArticleRevision.class);

        return articleEntityManager.post(id, revision, (entityManager, article) -> {
            validate(entityManager, article, ctx);
        });
    }

    public ArticleRevision meArticleRevisionGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        String revision = ctx.pathString("revision");

        return articleEntityManager.getRevision(id, revision, (entityManager, articleRevision) -> {
            validate(entityManager, articleRevision.getArticle(), ctx);
        });
    }

    public Article articleGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        return articleEntityManager.get(id, null);
    }

    public ArticleRevision articleRevisionGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        String revision = ctx.pathString("revision");
        if (revision.equals("latest")) {
            throw new ForbiddenException("latest not allowed");
        }
        return articleEntityManager.getRevision(id, revision, null);
    }
}
