package app.munch.query;

import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import dev.fuxing.jpa.EntityQuery;
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
        return query(cursor, query -> {
            query.where("m.profile.username = :username", "username", username);
        });
    }

    public TransportList query(TransportCursor cursor, Consumer<EntityQuery<ProfileMedia>> consumer) {
        return provider.reduce(true, entityManager -> {
            return query(entityManager, cursor, consumer)
                    .asTransportList((media, builder) -> {
                        builder.putAll(cursor);
                        builder.put("createdAt", media.getCreatedAt().getTime());
                        builder.put("id", media.getId());
                    });
        });
    }

    public static EntityQuery.EntityStream<ProfileMedia> query(EntityManager entityManager, TransportCursor cursor, Consumer<EntityQuery<ProfileMedia>> consumer) {
        return EntityQuery.select(entityManager, "SELECT m FROM ProfileMedia m", ProfileMedia.class)
                .where("m.status = :status", "status", ProfileMediaStatus.PUBLIC)
                .consume(consumer)
                .predicate(cursor.has("createdAt", "id"), query -> {
                    query.where("(m.createdAt < :createdAt OR (m.createdAt = :createdAt AND m.id < :id))",
                            "createdAt", cursor.getDate("createdAt"), "id", cursor.get("id"));
                })
                .orderBy("m.createdAt DESC, m.id DESC")
                .size(cursor.size(20, 40))
                .asStream()
                .peek(MediaQuery::clean);
    }

    /**
     * @param media to peek when querying, readOnly must be {@code true}
     */
    public static void clean(ProfileMedia media) {
        media.setMentions(null);
//        media.getMentions().forEach(mention -> {
//            mention.setMedia(null);
//        });
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
