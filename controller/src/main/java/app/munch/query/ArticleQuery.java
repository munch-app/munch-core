package app.munch.query;

import app.munch.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Fuxing Loh
 * @since 2019-10-09 at 12:26 am
 */
@Singleton
public final class ArticleQuery extends AbstractQuery {

    /**
     * @param cursor          for querying
     * @param profileSupplier to query from profile
     * @return TransportList with cursor information
     */
    public TransportList query(TransportCursor cursor, Function<EntityManager, Profile> profileSupplier) {
        ArticleStatus status = cursor.getEnum("status", ArticleStatus.class);
        return query(cursor, status, profileSupplier);
    }

    /**
     * @param cursor          for querying
     * @param status          explicit status to restrict querying to that status
     * @param profileSupplier to query from profile
     * @return TransportList with cursor information
     */
    public TransportList query(TransportCursor cursor, ArticleStatus status, Function<EntityManager, Profile> profileSupplier) {
        return provider.reduce(true, entityManager -> {
            Profile profile = profileSupplier.apply(entityManager);
            if (profile == null) throw new ForbiddenException();

            return query(entityManager, cursor, status, query -> {
                query.where("profile", profile);
            }).asTransportList();
        });
    }

    public static EntityStream<Article> query(EntityManager entityManager, TransportCursor cursor, ArticleStatus status, Consumer<EntityQuery<Article>> consumer) {
        return EntityQuery.select(entityManager, "FROM Article", Article.class)
                .size(cursor.size(10, 33))
                .consume(consumer)
                .where("status = :status", "status", status)
                .predicate(cursor.has("updatedAt", "id"), query -> {
                    query.where("(publishedAt < :publishedAt OR (publishedAt = :publishedAt AND id < :id))",
                            "publishedAt", cursor.getDate("publishedAt"), "id", cursor.get("id")
                    );
                })
                .orderBy("publishedAt DESC, id DESC")
                .asStream((article, builder) -> {
                    builder.putAll(cursor);
                    builder.put("id", article.getId());
                    builder.put("publishedAt", article.getPublishedAt().getTime());
                })
                .peek(article -> {
                    article.setContent(null);
                });
    }

    @Singleton
    public static final class ImageQuery {
        public TransportList<ImageGroup> query(EntityManager entityManager, Article article, TransportCursor cursor) {
            ArticleRevision revision = entityManager.createQuery("FROM ArticleRevision " +
                    "WHERE article = :article " +
                    "ORDER BY uid DESC ", ArticleRevision.class)
                    .setParameter("article", article)
                    .setMaxResults(1)
                    .getSingleResult();

            @SuppressWarnings("ConstantConditions") final int from = cursor.getInt("from", 0);
            final int size = 5;

            List<ImageGroup> groups = revision.getContent()
                    .stream()
                    .filter(node -> node.getType().equals("place"))
                    .map(node -> (ArticleModel.PlaceNode) node)
                    .map(placeNode -> placeNode.getAttrs().getPlace())
                    .filter(Objects::nonNull)
                    .map(ArticleModel.PlaceNode.Attrs.Place::getId)
                    .filter(Objects::nonNull)
                    .skip(from)
                    .limit(size)
                    .map(placeId -> query(entityManager, placeId))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return new TransportList<>(groups, size, (g, builder) -> {
                builder.put("from", from + size);
                builder.put("size", size);
            });
        }

        public ImageGroup query(EntityManager entityManager, String placeId) {
            Place place = entityManager.find(Place.class, placeId);
            Set<Image> images = new HashSet<>();

            entityManager.createQuery("FROM Mention " +
                    "WHERE place = :place " +
                    "AND status = :status " +
                    "AND type IN (:types)", Mention.class)
                    .setParameter("place", place)
                    .setParameter("status", MentionStatus.PUBLIC)
                    .setParameter("types", Set.of(MentionType.MEDIA, MentionType.POST))
                    .setMaxResults(10)
                    .getResultList()
                    .forEach(mention -> {
                        switch (mention.getType()) {
                            case POST:
                                images.addAll(mention.getPost().getImages());
                                return;

                            case MEDIA:
                                images.addAll(mention.getMedia().getImages());
                                return;

                            default:
                        }
                    });

            if (images.isEmpty()) {
                return null;
            }

            return new ImageGroup(place, new ArrayList<>(images));
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ImageGroup {
            private final Place place;
            private final List<Image> images;

            private ImageGroup(Place place, List<Image> images) {
                this.place = place;
                this.images = images;
            }

            public Place getPlace() {
                return place;
            }

            public List<Image> getImages() {
                return images;
            }
        }
    }
}
