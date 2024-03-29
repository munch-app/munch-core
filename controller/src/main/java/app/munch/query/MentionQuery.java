package app.munch.query;

import app.munch.model.Mention;
import app.munch.model.MentionStatus;
import app.munch.model.MentionType;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.jpa.EntityStream;
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
public final class MentionQuery extends AbstractQuery {

    /**
     * @param username of profile
     * @return TransportList with entity in json and cursor information for next
     */
    public TransportList queryByUsername(String username, TransportCursor cursor) {
        return query(cursor, query -> {
            query.where("profile.username = :username", "username", username);
        });
    }

    /**
     * @param id place id
     * @return TransportList with entity in json and cursor information for next
     */
    public TransportList queryByPlace(String id, TransportCursor cursor) {
        return query(cursor, query -> {
            query.where("place.id = :pid", "pid", id);
        });
    }

    public TransportList query(TransportCursor cursor, Consumer<EntityQuery<Mention>> consumer) {
        return provider.reduce(true, entityManager -> {
            return query(entityManager, cursor, consumer)
                    .asTransportList();
        });
    }

    public static EntityStream<Mention> query(EntityManager entityManager, TransportCursor cursor, Consumer<EntityQuery<Mention>> consumer) {
        return EntityQuery.select(entityManager, "FROM Mention", Mention.class)
                .where("status", MentionStatus.PUBLIC)
                .consume(consumer)
                .predicate(cursor.has("createdAt", "id"), query -> {
                    query.where("(createdAt < :createdAt OR (createdAt = :createdAt AND id < :id))",
                            "createdAt", cursor.getDate("createdAt"), "id", cursor.get("id"));
                })
                .predicate(cursor.has("types"), query -> {
                    query.where("type IN (:types)", "types", cursor.getEnums("types", MentionType.class));
                })
                .orderBy("createdAt DESC, id DESC")
                .size(cursor.size(10, 33))
                .asStream((mention, builder) -> {
                    builder.putAll(cursor);
                    builder.put("createdAt", mention.getCreatedAt().getTime());
                    builder.put("id", mention.getId());
                })
                .peek(MentionQuery::clean);
    }

    /**
     * @param mention to peek when querying, readOnly must be {@code true}
     */
    public static void clean(Mention mention) {
        switch (mention.getType()) {
            case MEDIA:
                mention.getMedia().setMentions(null);
                break;

            case ARTICLE:
                mention.getArticle().setContent(null);
                break;

            case POST:
                break;
        }
    }
}
