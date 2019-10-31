package app.munch.query;

import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import org.hibernate.Hibernate;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.function.Consumer;

/**
 * @author Fuxing Loh
 * @since 2019-10-09 at 2:11 pm
 */
@Singleton
public final class MediaQuery extends AbstractQuery {

    /**
     * @param username of profile
     * @return TransportList with entity in json and cursor information for next
     */
    public TransportList query(String username, TransportCursor cursor) {
        return provider.reduce(true, entityManager -> {
            return query(entityManager, cursor, query -> {
                query.where("profile.username = :username", "username", username);
            }).asTransportList();
        });
    }

    public static EntityStream<ProfileMedia> query(EntityManager entityManager, TransportCursor cursor, Consumer<EntityQuery<ProfileMedia>> consumer) {
        return EntityQuery.select(entityManager, "FROM ProfileMedia", ProfileMedia.class)
                .where("status", ProfileMediaStatus.PUBLIC)
                .consume(consumer)
                .predicate(cursor.has("createdAt", "id"), query -> {
                    query.where("(createdAt < :createdAt OR (createdAt = :createdAt AND id < :id))",
                            "createdAt", cursor.getDate("createdAt"), "id", cursor.get("id"));
                })
                .orderBy("createdAt DESC, id DESC")
                .size(cursor.size(20, 40))
                .asStream((media, builder) -> {
                    builder.putAll(cursor);
                    builder.put("createdAt", media.getCreatedAt().getTime());
                    builder.put("id", media.getId());
                })
                .peek(MediaQuery::clean);
    }

    /**
     * @param media to peek when querying, readOnly must be {@code true}
     */
    public static void clean(ProfileMedia media) {
        media.getMentions().forEach(mention -> {
            mention.setMedia(null);
        });
    }

    public static void initialize(ProfileMedia media) {
        if (media.getProfile() != null) {
            Hibernate.initialize(media.getProfile());
        }

        if (media.getImages() != null) {
            Hibernate.initialize(media.getImages());
        }

        if (media.getMentions() != null) {
            Hibernate.initialize(media.getMentions());
        }
    }
}
