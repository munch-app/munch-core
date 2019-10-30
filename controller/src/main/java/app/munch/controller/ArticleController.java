package app.munch.controller;

import app.munch.elastic.ElasticIndexPublisher;
import app.munch.exception.EditLockedException;
import app.munch.exception.RestrictionException;
import app.munch.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.transport.TransportError;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static app.munch.model.PlaceEditableField.*;

/**
 * Created by: Fuxing
 * Date: 2019-08-14
 * Time: 09:23
 */
@Singleton
public final class ArticleController extends AbstractController {
    private final PlaceController placeController;

    @Inject
    ArticleController(PlaceController placeController) {
        this.placeController = placeController;
    }

    public Article post(ArticleRevision revision, Profile.Supplier supplier) {
        return provider.reduce(entityManager -> {
            Profile profile = supplier.supply(entityManager);
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

    public PostResponse post(String articleId, ArticleRevision revision, @Nullable BiConsumer<EntityManager, Article> consumer) {
        return provider.reduce(entityManager -> {
            Article article = entityManager.find(Article.class, articleId);
            if (article == null) throw new NotFoundException();

            if (consumer != null) {
                consumer.accept(entityManager, article);
            }

            revision.setArticle(article);
            Image.EntityUtils.initialize(entityManager, revision.getImage(), revision::setImage);

            entityManager.persist(revision);

            if (revision.getPublished()) {
                List<TransportError> errors = placeController.publish(entityManager, revision);
                return new PostResponse(revision, errors);
            }

            return new PostResponse(revision, List.of());
        });
    }

    public Article patch(String id, JsonNode body, @Nullable BiConsumer<EntityManager, Article> consumer) {
        return provider.reduce(entityManager -> {
            Article article = entityManager.find(Article.class, id);
            if (article == null) throw new NotFoundException();

            if (consumer != null) {
                consumer.accept(entityManager, article);
            }

            // Patch the allowed fields
            article = EntityPatch.with(entityManager, article, body)
                    .lock()
                    .patch("status", ArticleStatus.class, (article1, status) -> {
                        if (status != ArticleStatus.DELETED) {
                            throw new ForbiddenException("You can only change status to deleted.");
                        }
                        article1.setStatus(ArticleStatus.DELETED);
                    })
                    .persist();

            // Remove all linked entity with article is delete.
            if (article.getStatus() == ArticleStatus.DELETED) {
                placeController.delete(entityManager, article);
            }

            return article;
        });
    }

    @NotNull
    public ArticleRevision find(String id, String uid, @Nullable BiConsumer<EntityManager, ArticleRevision> consumer) {
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

            if (articleRevision == null) {
                throw new NotFoundException();
            }

            if (consumer != null) {
                consumer.accept(entityManager, articleRevision);
            }

            initializeImage(entityManager, articleRevision);
            return articleRevision;
        });
    }

    @NotNull
    public Article find(String id, @Nullable BiConsumer<EntityManager, Article> consumer) {
        return provider.reduce(true, entityManager -> {
            return find(entityManager, id, consumer);
        });
    }

    @NotNull
    public Article find(EntityManager entityManager, String id, @Nullable BiConsumer<EntityManager, Article> consumer) {
        Article article = entityManager.find(Article.class, id);
        if (article == null) {
            throw new NotFoundException();
        }

        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new ForbiddenException();
        }

        if (consumer != null) {
            consumer.accept(entityManager, article);
        }

        initializeImage(entityManager, article);
        return article;
    }

    private static void initializeImage(EntityManager entityManager, ArticleModel article) {
        article.getContent().stream()
                .filter(node -> node.getType().equals("image"))
                .map(node -> (ArticleModel.ImageNode) node)
                .forEach(node -> {
                    ArticleModel.ImageNode.Attrs attrs = node.getAttrs();
                    Image.EntityUtils.initialize(entityManager, attrs::getImage, attrs::setImage);
                });
    }

    @Singleton
    public static final class PlaceController extends app.munch.controller.PlaceController {

        private final MentionController mentionController;

        @Inject
        PlaceController(LockingController locking, RestrictionController restriction, ElasticIndexPublisher documentQueue, MentionController mentionController) {
            super(locking, restriction, documentQueue);
            this.mentionController = mentionController;
        }

        void delete(EntityManager entityManager, Article article) {
            getArticlePlaces(entityManager, article).forEach(articlePlace -> {
                delete(entityManager, articlePlace);
            });
        }

        void delete(EntityManager entityManager, ArticlePlace articlePlace) {
            mentionController.delete(entityManager, articlePlace.getPlace(), articlePlace.getArticle());
            entityManager.remove(articlePlace);
        }

        /**
         * @param entityManager to use to publish
         * @param revision      to read places from
         * @return list of TransportError thrown when trying to publish places, to allow failure.
         */
        List<TransportError> publish(EntityManager entityManager, ArticleRevision revision) {
            Set<Place> successes = new HashSet<>();
            List<TransportError> errors = new ArrayList<>();

            // Publish all places in content
            getContentPlaces(revision).forEach(attrsPlace -> {
                try {
                    Place place = publish(entityManager, revision.getArticle(), attrsPlace);
                    if (place == null) return;

                    successes.add(place);
                    attrsPlace.setId(place.getId());
                    attrsPlace.setSlug(place.getSlug());
                } catch (RestrictionException | EditLockedException e) {
                    errors.add(e.toError());
                }
            });

            // Delete any that is not published
            getArticlePlaces(entityManager, revision.getArticle()).forEach(articlePlace -> {
                if (!successes.contains(articlePlace.getPlace())) {
                    entityManager.remove(articlePlace);
                }
            });

            return errors;
        }

        @Nullable
        private Place publish(EntityManager entityManager, Article article, ArticleModel.PlaceNode.Attrs.Place attrsPlace) {
            Place place = null;

            if (Boolean.TRUE.equals(article.getOptions().getPlacePublishing())) {
                place = publish(entityManager, article.getProfile(), attrsPlace);

                if (attrsPlace.getImage() != null) {
                    Image image = Image.EntityUtils.initialize(entityManager, attrsPlace.getImage());
                    mentionController.put(entityManager, place, image, article.getProfile());
                }
            } else if (attrsPlace.getId() != null) {
                place = entityManager.find(Place.class, attrsPlace.getId());
            }

            if (place == null) {
                return null;
            }

            // Persist ArticlePlace (New/Existing)
            ArticlePlace articlePlace = find(entityManager, article, place);
            if (articlePlace == null) {
                articlePlace = new ArticlePlace();
                articlePlace.setPlace(place);
                articlePlace.setArticle(article);
            }

            entityManager.persist(articlePlace);
            mentionController.put(entityManager, place, article, article.getProfile());
            return place;
        }

        private Place publish(EntityManager entityManager, Profile profile, ArticleModel.PlaceNode.Attrs.Place attrsPlace) {
            if (attrsPlace.getId() != null) {
                return update(entityManager, profile, attrsPlace.getId(), attrsPlace,
                        Set.of(NAME, PHONE, WEBSITE, PRICE, LOCATION_ADDRESS, LOCATION_LAT_LNG, STATUS, TAGS, HOURS)
                );
            } else {
                return create(entityManager, profile, attrsPlace,
                        Set.of(NAME, PHONE, WEBSITE, PRICE, LOCATION_ADDRESS, LOCATION_LAT_LNG, STATUS, TAGS, HOURS)
                );
            }
        }

        private static ArticlePlace find(EntityManager entityManager, Article article, Place place) {
            List<ArticlePlace> list = entityManager.createQuery("FROM ArticlePlace " +
                    "WHERE article.id = :id AND place = :place", ArticlePlace.class)
                    .setParameter("id", article.getId())
                    .setParameter("place", place)
                    .getResultList();

            if (list.isEmpty()) {
                return null;
            }

            return list.get(0);
        }

        private static List<ArticleModel.PlaceNode.Attrs.Place> getContentPlaces(ArticleRevision revision) {
            return revision.getContent().stream()
                    .filter(node -> node.getType().equals("place"))
                    .map(n -> (ArticleModel.PlaceNode) n)
                    .map(placeNode -> placeNode.getAttrs().getPlace())
                    .collect(Collectors.toList());
        }

        private static List<ArticlePlace> getArticlePlaces(EntityManager entityManager, Article article) {
            return entityManager.createQuery("FROM ArticlePlace " +
                    "WHERE article.id = :id", ArticlePlace.class)
                    .setParameter("id", article.getId())
                    .getResultList();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class PostResponse {
        private final String id;
        private final String uid;
        private final List<TransportError> errors;

        private PostResponse(ArticleRevision revision, List<TransportError> errors) {
            this.id = revision.getId();
            this.uid = revision.getUid();
            this.errors = errors;
        }

        public String getId() {
            return id;
        }

        public String getUid() {
            return uid;
        }

        public List<TransportError> getErrors() {
            return errors;
        }
    }
}
