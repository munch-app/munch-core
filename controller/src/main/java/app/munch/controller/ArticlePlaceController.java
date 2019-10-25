package app.munch.controller;

import app.munch.exception.DataLockedException;
import app.munch.exception.RestrictionException;
import app.munch.model.*;
import dev.fuxing.jpa.TransactionProvider;
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
import java.util.stream.Collectors;

import static app.munch.model.PlaceEditableField.*;
import static app.munch.model.PlaceEditableField.HOURS;

/**
 * Created by: Fuxing
 * Date: 1/9/19
 * Time: 9:53 pm
 */
@Singleton
public final class ArticlePlaceController {
    private final TransactionProvider provider;
    private final PlaceController placeController;

    @Inject
    ArticlePlaceController(TransactionProvider provider, PlaceController placeController) {
        this.provider = provider;
        this.placeController = placeController;
    }

    public void deleteAll(String articleId) {
        provider.with(entityManager -> {
            entityManager.createQuery("DELETE FROM ArticlePlace " +
                    "WHERE article.id = :articleId")
                    .setParameter("articleId", articleId)
                    .executeUpdate();
        });
    }

    /**
     * Note: you are passing in Detached entities
     */
    public List<Response> populateAll(String profileUid, ArticleRevision revision) {
        List<ArticleModel.PlaceNode.Attrs.Place> attrsPlaces = revision.getContent().stream()
                .filter(node -> node.getType().equals("place"))
                .map(n -> (ArticleModel.PlaceNode) n)
                .map(placeNode -> placeNode.getAttrs().getPlace())
                .collect(Collectors.toList());

        List<Response> responses = new ArrayList<>();

        // Set<PlaceIds> is used to prevent duplicate of `ArticlePlace` linking to same `Place`
        Set<String> placeIds = new HashSet<>();

        provider.with(entityManager -> {
            // Map all AttrsPlace to Map and populate ArticleRevision.content
            attrsPlaces.forEach(attrsPlace -> {
                Response response = new Response(attrsPlace);
                try {
                    // PublishPlace and get a copy of the representing place
                    Profile profile = Profile.findByUid(entityManager, profileUid);
                    Place place = publishPlace(entityManager, profile, revision.getOptions(), attrsPlace);
                    if (place != null) {
                        placeIds.add(place.getId());
                        attrsPlace.setId(place.getId());
                        attrsPlace.setSlug(place.getSlug());

                        if (attrsPlace.getImage() != null) {
                            publishImage(entityManager, revision.getOptions(), place, attrsPlace.getImage());
                        }
                    }
                } catch (RestrictionException | DataLockedException e) {
                    response.error = e.toError();
                }
                responses.add(response);
            });

            // Persist Revision content, copy from detached entity and paste over,
            // because UserType is JSONB it's won't corrupt the data
            // Cascade.Persist will cascade to 'ArticleRevision' & 'Article'
            ArticleRevision revisionEntity = entityManager.find(ArticleRevision.class, revision.getUid());
            revisionEntity.setContent(revision.getContent());
            revisionEntity.getArticle().setContent(revision.getContent());
            entityManager.persist(revisionEntity);

            // Find all ArticlePlace, Delete/Create any that is required
            List<ArticlePlace> articlePlaces = entityManager.createQuery("FROM ArticlePlace " +
                    "WHERE article.id = :articleId", ArticlePlace.class)
                    .setParameter("articleId", revision.getId())
                    .getResultList();

            // At this stage, only placeIds that exists is a real place

            // Delete any that is not mapped, ensures only one copy.
            for (ArticlePlace articlePlace : articlePlaces) {
                if (placeIds.contains(articlePlace.getPlace().getId())) {
                    placeIds.remove(articlePlace.getPlace().getId());
                } else {
                    entityManager.remove(articlePlace);
                }
            }

            // Create any that do not already exist
            for (String placeId : placeIds) {
                ArticlePlace articlePlace = new ArticlePlace();
                articlePlace.setArticle(entityManager.getReference(Article.class, revision.getId()));
                articlePlace.setPlace(entityManager.getReference(Place.class, placeId));
                entityManager.persist(articlePlace);
            }
        });

        return responses;
    }

    @Nullable
    private Place publishPlace(EntityManager entityManager, Profile profile, Article.Options options, ArticleModel.PlaceNode.Attrs.Place attrsPlace) {
        // If publishing is enabled, publish place
        if (options.getPlacePublishing() != null) {
            if (attrsPlace.getId() != null) {
                return placeController.update(entityManager, profile, attrsPlace.getId(), attrsPlace,
                        Set.of(NAME, PHONE, WEBSITE, PRICE, LOCATION_ADDRESS, LOCATION_LAT_LNG, STATUS, TAGS, HOURS)
                );
            } else {
                return placeController.create(entityManager, profile, attrsPlace,
                        Set.of(NAME, PHONE, WEBSITE, PRICE, LOCATION_ADDRESS, LOCATION_LAT_LNG, STATUS, TAGS, HOURS)
                );
            }
        }

        // else if placeId exist, fetch and return
        if (attrsPlace.getId() != null) {
            return entityManager.find(Place.class, attrsPlace.getId());
        }

        // All else, just return null, don't need to route to Place
        return null;
    }

    /**
     * Publish Image in Attrs.Place into PlaceImage linked to the Place
     */
    private void publishImage(EntityManager entityManager, Article.Options options, Place place, Image detachedImage) {
        // Need to use Mention Engine to Deal with Image as we scale

        if (options.getPlacePublishing() == null || !options.getPlacePublishing()) {
            return;
        }

        Image.EntityUtils.initialize(entityManager, detachedImage, image -> {
            List list = entityManager.createQuery("FROM PlaceImage " +
                    "WHERE place.id = :placeId AND image.uid = :imageId")
                    .setParameter("placeId", place.getId())
                    .setParameter("imageId", image.getUid())
                    .getResultList();

            if (!list.isEmpty()) {
                // Don't add if already exist
                return;
            }

            PlaceImage placeImage = new PlaceImage();
            placeImage.setPlace(place);
            placeImage.setImage(image);
            entityManager.persist(placeImage);

            // Publish Image will fill Place.Image if it don't already exist
            if (place.getImage() == null) {
                place.setImage(image);
            }
        });
    }

    /**
     * Response object for ArticlePlace mutation
     */
    public static final class Response {

        @NotNull
        private final ArticleModel.PlaceNode.Attrs.Place place;
        private TransportError error = null;

        public Response(@NotNull ArticleModel.PlaceNode.Attrs.Place place) {
            this.place = place;
        }

        public ArticleModel.PlaceNode.Attrs.Place getPlace() {
            return place;
        }

        public TransportError getError() {
            return error;
        }
    }
}
