package app.munch.query;

import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.function.Consumer;

/**
 * @author Fuxing Loh
 * @since 2019-10-09 at 2:11 pm
 */
@Singleton
public final class MediaQuery extends Query {

    /**
     * @param username of profile
     * @return TransportList with entity in json and cursor information for next
     */
    public TransportList query(String username, TransportCursor cursor) {
        return query(cursor, query -> {
            query.where("profile.username = :username", "username", username);
        });
    }

    public TransportList query(TransportCursor cursor, Consumer<EntityQuery<ProfileMedia>> consumer) {
        return provider.reduce(true, entityManager -> {
            return query(entityManager, cursor, consumer)
                    .asTransportList((mention, builder) -> {
                        builder.putAll(cursor);
                        builder.put("createdAt", mention.getCreatedAt().getTime());
                        builder.put("id", mention.getId());
                    });
        });
    }

    public ProfileMedia find(String id) {
        return provider.reduce(true, entityManager -> {
            ProfileMedia media = entityManager.find(ProfileMedia.class, id);
            if (media == null) {
                return null;
            }

            if (media.getStatus() != ProfileMediaStatus.PUBLIC) {
                throw new ForbiddenException();
            }

            HibernateUtils.initialize(media.getProfile());
            HibernateUtils.initialize(media.getImages());
            HibernateUtils.initialize(media.getMentions());
            peek(media);
            return media;
        });
    }

    public static EntityQuery<ProfileMedia>.EntityStream query(EntityManager entityManager, TransportCursor cursor, Consumer<EntityQuery<ProfileMedia>> consumer) {
        return EntityQuery.select(entityManager, "FROM ProfileMedia", ProfileMedia.class)
                .where("status", ProfileMediaStatus.PUBLIC)
                .consume(consumer)
                .predicate(cursor.has("createdAt", "id"), query -> {
                    query.where("(createdAt < :createdAt OR (createdAt = :createdAt AND id < :id))",
                            "createdAt", cursor.getDate("createdAt"), "id", cursor.get("id"));
                })
                .orderBy("createdAt DESC, id DESC")
                .size(cursor.size(20, 40))
                .asStream()
                .peek(MediaQuery::peek);
    }

    /**
     * @param media to peek when querying, readOnly must be {@code true}
     */
    private static void peek(ProfileMedia media) {
        media.getMentions().forEach(mention -> {
            mention.setMedia(null);
        });
    }
}