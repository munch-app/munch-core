package app.munch.controller;

import app.munch.model.*;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

/**
 * @author Fuxing Loh
 * @since 2019-10-25 at 15:07
 */
@Singleton
public class MentionController extends Controller {

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

    public Mention put(EntityManager entityManager, Place place, Article article, Profile createdBy) {
        Mention mention = find(entityManager, place, "article", article);
        if (mention != null) {
            return mention;
        }

        return add(entityManager, place, article, createdBy);
    }

    @Nullable
    public Mention delete(EntityManager entityManager, Place place, Article article) {
        Mention mention = find(entityManager, place, "article", article);
        if (mention == null) {
            return null;
        }

        entityManager.remove(mention);
        return mention;
    }

    public Mention add(Place place, PlacePost post, Profile.Supplier createdBy) {
        return provider.reduce(entityManager -> {
            return add(entityManager, place, post, createdBy.supply(entityManager));
        });
    }

    public Mention add(EntityManager entityManager, Place place, PlacePost post, Profile createdBy) {
        Objects.requireNonNull(place);
        Objects.requireNonNull(post);

        Mention mention = new Mention();
        mention.setPlace(place);
        mention.setProfile(post.getProfile().attach(entityManager));

        mention.setType(MentionType.POST);
        mention.setPost(post);

        mention.setStatus(MentionStatus.PUBLIC);
        mention.setCreatedBy(createdBy);

        entityManager.persist(mention);
        return mention;
    }

    public Mention put(EntityManager entityManager, Place place, Image image, Profile createdBy) {
        List<PlacePost> posts = entityManager.createQuery("FROM PlacePost " +
                "WHERE :image IN images " +
                "AND place = :place", PlacePost.class)
                .setParameter("place", place)
                .setParameter("image", image)
                .getResultList();

        if (!posts.isEmpty()) {
            return find(entityManager, place, "post", posts.get(0));
        }

        PlacePost post = new PlacePost();
        post.setPlace(place);
        post.setImages(List.of(image));
        post.setProfile(image.getProfile());
        post.setStatus(PlacePostStatus.PUBLISHED);
        return add(entityManager, place, post, createdBy);
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

    static Mention find(EntityManager entityManager, Place place, String name, Object object) {
        List<Mention> mentions = entityManager.createQuery("FROM Mention " +
                "WHERE " + name + " = :parameter " +
                "AND place = :place", Mention.class)
                .setParameter("place", place)
                .setParameter("parameter", object)
                .getResultList();

        if (mentions.isEmpty()) {
            return null;
        }

        return mentions.get(0);
    }
}
