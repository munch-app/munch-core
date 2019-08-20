package app.munch.manager;

import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public TransportList list(ArticleStatus status, Function<EntityManager, Profile> profileSupplier, int size, TransportCursor cursor) {
        String cursorId = cursor.get("id");
        Long cursorUpdatedAt = cursor.getLong("updatedAt");
        return provider.reduce(true, entityManager -> {
            Profile profile = profileSupplier.apply(entityManager);
            if (profile == null) throw new ForbiddenException();

            return EntityStream.of(() -> {
                if (cursorId != null && cursorUpdatedAt != null) {
                    return entityManager.createQuery("FROM Article " +
                            "WHERE profile.id = :profileId AND status = :status " +
                            "AND (updatedAt < :cursorUpdatedAt OR (updatedAt = :cursorUpdatedAt AND id < :cursorId)) " +
                            "ORDER BY updatedAt DESC, id DESC", Article.class)
                            .setParameter("profileId", profile.getId())
                            .setParameter("status", status)
                            .setParameter("cursorUpdatedAt", new Date(cursorUpdatedAt))
                            .setParameter("cursorId", cursorId)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Article " +
                        "WHERE profile.id = :profileId AND status = :status " +
                        "ORDER BY updatedAt DESC, id DESC", Article.class)
                        .setParameter("profileId", profile.getId())
                        .setParameter("status", status)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (article, builder) -> {
                builder.put("id", article.getId());
                builder.put("updatedAt", article.getUpdatedAt().getTime());
            }).asTransportList();
        });
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
            findEntities(entityManager, revision);

            if (revision.getPublished()) {
                addArticlePlace(entityManager, article, ArticleStatus.PUBLISHED);
            }

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
                    .patch("status", ArticleStatus.class, Article::setStatus)
                    .persist(entity -> {
                        addArticlePlace(entityManager, entity, article.getStatus());
                        entityManager.persist(entity);
                    });
        });
    }

    public Article get(String id, @Nullable BiConsumer<EntityManager, Article> consumer) {
        return provider.reduce(true, entityManager -> {
            Article article = entityManager.find(Article.class, id);
            if (article == null) throw new NotFoundException();

            if (consumer != null) {
                consumer.accept(entityManager, article);
            }
            return article;
        });
    }

    public ArticleRevision getRevision(String id, String revision, @Nullable BiConsumer<EntityManager, ArticleRevision> consumer) {
        return provider.reduce(true, entityManager -> {
            ArticleRevision articleRevision;

            if (revision.equals("latest")) {
                articleRevision = entityManager.createQuery("FROM ArticleRevision " +
                        "WHERE article.id = :id " +
                        "ORDER BY revision DESC ", ArticleRevision.class)
                        .setParameter("id", id)
                        .setMaxResults(1)
                        .getSingleResult();
            } else {
                articleRevision = entityManager.createQuery("FROM ArticleRevision " +
                        "WHERE article.id = :id AND revision = :revision", ArticleRevision.class)
                        .setParameter("id", id)
                        .setParameter("revision", revision)
                        .getSingleResult();
            }

            if (articleRevision == null) throw new NotFoundException();

            if (consumer != null) {
                consumer.accept(entityManager, articleRevision);
            }

            return articleRevision;
        });
    }

    private void addArticlePlace(EntityManager entityManager, Article article, ArticleStatus status) {
        if (status == ArticleStatus.DELETED) {
            entityManager.createQuery("DELETE FROM ArticlePlace " +
                    "WHERE article.id = :articleId")
                    .setParameter("articleId", article.getId())
                    .executeUpdate();
            return;
        }

        if (status == ArticleStatus.PUBLISHED) {
            List<ArticlePlace> places = entityManager.createQuery("FROM ArticlePlace " +
                    "WHERE article.id = :articleId", ArticlePlace.class)
                    .setParameter("articleId", article.getId())
                    .getResultList();

            long millis = System.currentTimeMillis();
            AtomicLong position = new AtomicLong(millis);

            // TODO(fuxing): Respect ArticleModel.Options
            // TODO(fuxing): This needs to be changed in Partner 2
            article.getContent().stream()
                    .filter(node -> node.getType().equals("place"))
                    .map(n -> (ArticleModel.PlaceNode) n)
                    .forEach(node -> {
                        ArticlePlace place = node.getAttrs().getPlace();
                        Objects.requireNonNull(place);
                        String placeId = Objects.requireNonNull(place.getPlace().getId());

                        places.stream()
                                .filter(articlePlace -> articlePlace.getPlace().getId().equals(placeId))
                                .findFirst()
                                .ifPresentOrElse(articlePlace -> {
                                    // Update
                                    articlePlace.setPosition(position.getAndDecrement());
                                    articlePlace.setUpdatedAt(new Timestamp(millis));
                                    entityManager.persist(articlePlace);
                                }, () -> {
                                    // Create
                                    ArticlePlace articlePlace = new ArticlePlace();
                                    articlePlace.setPlace(entityManager.find(Place.class, placeId));
                                    articlePlace.setArticle(article);

                                    articlePlace.setPosition(position.getAndDecrement());
                                    articlePlace.setUpdatedAt(new Timestamp(millis));
                                    entityManager.persist(articlePlace);
                                });
                    });

            // Remove the rest
            places.stream()
                    .filter(articlePlace -> articlePlace.getUpdatedAt().getTime() != millis)
                    .forEach(entityManager::remove);
        }
    }

    private void findEntities(EntityManager entityManager, ArticleModel model) {
        if (model.getContent() == null) return;

        if (model.getImage() != null && model.getImage().getId() != null) {
            model.setImage(entityManager.find(Image.class, model.getImage().getId()));
        }


        model.getContent().stream().takeWhile(Objects::nonNull).forEach(node -> {
            if (node instanceof ArticleModel.ImageNode) {
                ArticleModel.ImageNode.Attrs attrs = ((ArticleModel.ImageNode) node).getAttrs();
                if (attrs == null) return;
                if (attrs.getImage() == null) return;
                if (attrs.getImage().getId() == null) return;

                attrs.setImage(entityManager.find(Image.class, attrs.getImage().getId()));
            } else if (node instanceof ArticleModel.AvatarNode) {
                ArticleModel.AvatarNode.Attrs attrs = ((ArticleModel.AvatarNode) node).getAttrs();
                if (attrs == null) return;
                if (attrs.getImages() == null) return;
                if (attrs.getImages().isEmpty()) return;

                List<Image> images = attrs.getImages()
                        .stream()
                        .map(image -> entityManager.find(Image.class, image.getId()))
                        .collect(Collectors.toList());
                attrs.setImages(images);
            } else if (node instanceof ArticleModel.PlaceNode) {
                ArticleModel.PlaceNode.Attrs attrs = ((ArticleModel.PlaceNode) node).getAttrs();
                if (attrs == null) return;

                // TODO(fuxing): Resolve Place??
            }
        });
    }
}
