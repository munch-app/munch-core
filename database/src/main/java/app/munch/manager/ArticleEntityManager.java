package app.munch.manager;

import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.TransactionProvider;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by: Fuxing
 * Date: 2019-08-14
 * Time: 09:23
 */
@Singleton
public final class ArticleEntityManager {
    private final TransactionProvider provider;

    @Inject
    ArticleEntityManager(TransactionProvider provider) {
        this.provider = provider;
    }

    public Article post(ArticleRevision revision, Function<EntityManager, Profile> profileSupplier) {
        return provider.reduce(entityManager -> {
            Profile profile = profileSupplier.apply(entityManager);
            if (profile == null) throw new ForbiddenException();

            Article article = new Article();
            article.setStatus(ArticleStatus.DRAFT);
            article.setProfile(profile);

            revision.setArticle(article);
            revision.setPublished(false);
            entityManager.persist(revision);
            return article;
        });
    }

    public ArticleRevision post(String articleId, ArticleRevision revision, @Nullable BiConsumer<EntityManager, Article> consumer) {
        return provider.reduce(entityManager -> {
            Article article = entityManager.find(Article.class, articleId);
            if (article == null) throw new NotFoundException();

            if (consumer != null) {
                consumer.accept(entityManager, article);
            }

            revision.setArticle(article);

            Image.EntityUtils.initialize(entityManager, revision.getImage(), revision::setImage);

            entityManager.persist(revision);
            return revision;
        });
    }

    public Article patch(String id, JsonNode body, @Nullable BiConsumer<EntityManager, Article> consumer) {
        return provider.reduce(entityManager -> {
            Article article = entityManager.find(Article.class, id);
            if (article == null) throw new NotFoundException();

            if (consumer != null) {
                consumer.accept(entityManager, article);
            }

            return EntityPatch.with(entityManager, article, body)
                    .lock()
                    .patch("status", ArticleStatus.class, (article1, status) -> {
                        if (status != ArticleStatus.DELETED) {
                            throw new ForbiddenException("You can only change status to deleted.");
                        }
                        article1.setStatus(ArticleStatus.DELETED);
                    })
                    .persist();
        });
    }

    public ArticleRevision get(String id, String uid, @Nullable BiConsumer<EntityManager, ArticleRevision> consumer) {
        return provider.reduce(true, entityManager -> {
            ArticleRevision articleRevision;

            if (uid.equals("latest")) {
                articleRevision = entityManager.createQuery("FROM ArticleRevision " +
                        "WHERE article.id = :id " +
                        "ORDER BY uid DESC ", ArticleRevision.class)
                        .setParameter("id", id)
                        .setMaxResults(1)
                        .getSingleResult();
            } else {
                articleRevision = entityManager.createQuery("FROM ArticleRevision " +
                        "WHERE article.id = :id AND uid = :uid", ArticleRevision.class)
                        .setParameter("id", id)
                        .setParameter("uid", uid)
                        .getSingleResult();
            }

            if (articleRevision == null) throw new NotFoundException();

            if (consumer != null) {
                consumer.accept(entityManager, articleRevision);
            }

            return articleRevision;
        });
    }
}
