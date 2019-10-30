package app.munch.query;

import app.munch.jpa.QueryBuilder;
import app.munch.model.ProfileMedia;
import app.munch.model.ProfileMediaStatus;
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
        return query(cursor, query -> {
            query.where("username", username);
        });
    }

    public TransportList query(TransportCursor cursor, Consumer<QueryBuilder<ProfileMedia>> consumer) {
        return provider.reduce(true, entityManager -> {
            return query(entityManager, cursor, consumer).asTransportList();
        });
    }

    public static EntityStream<ProfileMedia> query(EntityManager entityManager, TransportCursor cursor, Consumer<QueryBuilder<ProfileMedia>> consumer) {
        return QueryBuilder.from(ProfileMedia.class, entityManager)
                .select("id", "profile", "type", "status", "images", "content", "mentions", "updatedAt", "createdAt")
                .where("status", ProfileMediaStatus.PUBLIC)
                .with(consumer)
                .max(cursor.size(20, 40))
                .cursorAtIdDesc(cursor, "createdAt", "id");
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
