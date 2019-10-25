package app.munch.controller;

import app.munch.model.*;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Objects;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 15:07
 */
@Singleton
public final class MentionController extends Controller {

    public Mention add(Place place, Article article, Profile.Supplier createdBy) {
        return provider.reduce(entityManager -> {
            return add(entityManager, place, article, createdBy.supply(entityManager));
        });
    }

    public Mention add(EntityManager entityManager, Place place, Article article, Profile createdBy) {
        Objects.requireNonNull(place);
        Objects.requireNonNull(article);

        Mention mention = new Mention();
        mention.setPlace(place);
        mention.setProfile(article.getProfile().attach(entityManager));

        mention.setType(MentionType.ARTICLE);
        mention.setArticle(article);

        mention.setStatus(MentionStatus.PUBLIC);
        mention.setCreatedBy(createdBy);

        entityManager.persist(mention);
        return mention;
    }

    public Mention add(Place place, PlaceImage image, Profile.Supplier createdBy) {
        return provider.reduce(entityManager -> {
            return add(entityManager, place, image, createdBy.supply(entityManager));
        });
    }

    public Mention add(EntityManager entityManager, Place place, PlaceImage image, Profile createdBy) {
        Objects.requireNonNull(place);
        Objects.requireNonNull(image);

        Mention mention = new Mention();
        mention.setPlace(place);
        mention.setProfile(image.getProfile().attach(entityManager));

        mention.setType(MentionType.IMAGE);
        // TODO(fuxing): Set when decided
//        mention.setImage

        mention.setStatus(MentionStatus.PUBLIC);
        mention.setCreatedBy(createdBy);

        entityManager.persist(mention);
        return mention;
    }

    public Mention add(Place place, ProfileMedia media, Profile.Supplier createdBy) {
        return provider.reduce(entityManager -> {
            return add(entityManager, place, media, createdBy.supply(entityManager));
        });
    }

    public Mention add(EntityManager entityManager, Place place, ProfileMedia media, Profile createdBy) {
        Objects.requireNonNull(place);
        Objects.requireNonNull(media);

        Mention mention = new Mention();
        mention.setPlace(place);
        mention.setProfile(media.getProfile().attach(entityManager));

        mention.setType(MentionType.MEDIA);
        mention.setMedia(media);

        mention.setStatus(MentionStatus.PUBLIC);
        mention.setCreatedBy(createdBy);

        entityManager.persist(mention);
        return mention;
    }

}
