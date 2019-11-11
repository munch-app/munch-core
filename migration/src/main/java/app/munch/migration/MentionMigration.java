package app.munch.migration;

import app.munch.database.DatabaseModule;
import app.munch.model.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.jpa.TransactionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 2019-11-09 at 19:44
 */
public final class MentionMigration {
    private static final Logger logger = LoggerFactory.getLogger(MentionMigration.class);
    private final TransactionProvider provider;

    private int counter = 0;

    @Inject
    public MentionMigration(TransactionProvider provider) {
        this.provider = provider;
    }

    public void moveArticle() {
        String uid = moveArticle(null, 10);
        while (uid != null) {
            uid = moveArticle(uid, 10);
        }
    }

    private String moveArticle(String uid, int size) {
        return provider.reduce(entityManager -> {
            EntityQuery<Tuple> query = EntityQuery.select(entityManager, "SELECT article.id, place.id, article.profile.uid, uid FROM ArticlePlace", Tuple.class);
            query.orderBy("uid DESC");
            query.size(size);

            if (uid != null) {
                query.where("uid < :uid", "uid", uid);
            }

            Profile createdBy = Profile.findByUid(entityManager, Profile.COMPAT_ID);
            List<Tuple> tuples = query.asList();
            for (Tuple tuple : tuples) {
                logger.info("Article: {}", ++counter);
                persist(entityManager, createdBy, tuple.get(0, String.class), tuple.get(1, String.class), tuple.get(2, String.class));
            }

            if (tuples.size() == size) {
                return tuples.get(size - 1).get(3, String.class);
            }
            return null;
        });
    }

    private void persist(EntityManager entityManager, Profile createdBy, String articleId, String placeId, String profileUid) {
        List list = entityManager.createQuery("FROM Mention WHERE place.id = :placeId AND article.id = :articleId")
                .setParameter("placeId", placeId)
                .setParameter("articleId", articleId)
                .getResultList();

        if (!list.isEmpty()) {
            logger.info("Article Already Added");
            return;
        }

        Mention mention = new Mention();
        mention.setPlace(entityManager.getReference(Place.class, placeId));
        mention.setProfile(entityManager.getReference(Profile.class, profileUid));
        mention.setArticle(entityManager.getReference(Article.class, articleId));
        mention.setType(MentionType.ARTICLE);
        mention.setStatus(MentionStatus.PUBLIC);
        mention.setCreatedBy(createdBy);

        entityManager.persist(mention);
        entityManager.flush();
        logger.info("Added Article");
    }

    public void moveImage() {
        counter = 0;
        String uid = moveImage(null, 10);
        while (uid != null) {
            uid = moveImage(uid, 10);
        }
    }

    private String moveImage(String id, int size) {
        return provider.reduce(entityManager -> {
            EntityQuery<Tuple> query = EntityQuery.select(entityManager, "SELECT id FROM Place", Tuple.class);
            query.orderBy("id DESC");
            query.size(size);

            if (id != null) {
                query.where("id < :id", "id", id);
            }

            Profile createdBy = Profile.findByUid(entityManager, Profile.COMPAT_ID);
            List<Tuple> tuples = query.asList();
            for (Tuple tuple : tuples) {
                logger.info("Place: {}", ++counter);

                List<Image> images = entityManager.createQuery("SELECT pi.image FROM PlaceImage pi WHERE pi.place.id = :placeId", Image.class)
                        .setParameter("placeId", tuple.get(0, String.class))
                        .setMaxResults(10)
                        .getResultList();

                if (images.isEmpty()) {
                    continue;
                }

                persist(entityManager, createdBy, tuple.get(0, String.class), images);
            }

            if (tuples.size() == size) {
                return tuples.get(size - 1).get(0, String.class);
            }
            return null;
        });
    }

    private void persist(EntityManager entityManager, Profile createdBy, String placeId, List<Image> images) {
        List list = entityManager.createQuery("FROM Mention WHERE place.id = :placeId AND type = :types")
                .setParameter("placeId", placeId)
                .setParameter("types", MentionType.POST)
                .getResultList();

        if (!list.isEmpty()) {
            return;
        }

        Place place = entityManager.getReference(Place.class, placeId);

        PlacePost post = new PlacePost();
        post.setStatus(PlacePostStatus.PUBLISHED);
        post.setProfile(createdBy);
        post.setPlace(place);
        post.setImages(images);

        entityManager.persist(post);
        entityManager.flush();

        Mention mention = new Mention();
        mention.setPlace(place);
        mention.setProfile(createdBy);
        mention.setPost(post);
        mention.setType(MentionType.POST);
        mention.setStatus(MentionStatus.PUBLIC);
        mention.setCreatedBy(createdBy);

        entityManager.persist(mention);
        entityManager.flush();
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DatabaseModule());
        MentionMigration migration = injector.getInstance(MentionMigration.class);
        migration.moveArticle();
        migration.moveImage();
    }
}
