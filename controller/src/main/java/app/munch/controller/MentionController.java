package app.munch.controller;

import app.munch.model.Mention;
import app.munch.model.MentionStatus;
import app.munch.model.MentionType;
import app.munch.model.Profile;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import org.intellij.lang.annotations.Language;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Date: 28/9/19
 * Time: 10:44 pm
 *
 * @author Fuxing Loh
 */
@Singleton
public final class MentionController extends Controller {

    public TransportList queryByPlace(String id, int size, Set<MentionType> types, TransportCursor cursor) {
        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (types.isEmpty()) {
                    return selectByPlace(entityManager, id, size, cursor);
                }
                return selectByPlace(entityManager, id, size, types, cursor);
            }).cursor(size, (mention, builder) -> {
                builder.put("createdAt", mention.getCreatedAt().getTime());
                builder.put("id", mention.getId());
            }).asTransportList();
        });
    }

    private List<Mention> selectByPlace(EntityManager entityManager, String placeId, int size, Set<MentionType> types, TransportCursor cursor) {
        final Long createdAt = cursor.getLong("createdAt");
        final String id = cursor.get("id");

        if (createdAt != null && id != null) {
            return entityManager.createQuery("FROM Mention " +
                    "WHERE status = :status AND place.id = :placeId AND type IN (:types) " +
                    "AND (createdAt < :createdAt OR (createdAt = :createdAt AND id < :id)) " +
                    "ORDER BY createdAt DESC, id DESC ", Mention.class)
                    .setParameter("status", MentionStatus.LINKED)
                    .setParameter("placeId", placeId)
                    .setParameter("types", types)
                    .setParameter("createdAt", new Date(createdAt))
                    .setParameter("id", id)
                    .setMaxResults(size)
                    .getResultList();
        }

        return entityManager.createQuery("FROM Mention " +
                "WHERE status = :status AND place.id = :placeId AND type IN (:types) " +
                "ORDER BY createdAt DESC, id DESC ", Mention.class)
                .setParameter("status", MentionStatus.LINKED)
                .setParameter("placeId", placeId)
                .setParameter("types", types)
                .setMaxResults(size)
                .getResultList();
    }

    private List<Mention> selectByPlace(EntityManager entityManager, String placeId, int size, TransportCursor cursor) {
        final Long createdAt = cursor.getLong("createdAt");
        final String id = cursor.get("id");

        if (createdAt != null && id != null) {
            return entityManager.createQuery("SELECT m FROM Mention m " +
                    "WHERE m.status = :status AND m.place.id = :placeId " +
                    "AND (createdAt < :createdAt OR (createdAt = :createdAt AND id < :id)) " +
                    "ORDER BY createdAt DESC, id DESC ", Mention.class)
                    .setParameter("status", MentionStatus.LINKED)
                    .setParameter("placeId", placeId)
                    .setParameter("createdAt", new Date(createdAt))
                    .setParameter("id", id)
                    .setMaxResults(size)
                    .getResultList();
        }

        return entityManager.createQuery("SELECT m FROM Mention m " +
                "WHERE m.status = :status AND m.place.id = :placeId " +
                "ORDER BY createdAt DESC, id DESC ", Mention.class)
                .setParameter("status", MentionStatus.LINKED)
                .setParameter("placeId", placeId)
                .setMaxResults(size)
                .getResultList();
    }

    public TransportList queryByUsername(String username, int size, Set<MentionType> types, TransportCursor cursor) {
        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (types.isEmpty()) {
                    return selectByUsername(entityManager, username, size, cursor);
                }
                return selectByUsername(entityManager, username, size, types, cursor);
            }).cursor(size, (mention, builder) -> {
                builder.put("createdAt", mention.getCreatedAt().getTime());
                builder.put("id", mention.getId());
            }).asTransportList();
        });
    }

    private List<Mention> selectByUsername(EntityManager entityManager, String username, int size, Set<MentionType> types, TransportCursor cursor) {
        final Long createdAt = cursor.getLong("createdAt");
        final String id = cursor.get("id");

        if (createdAt != null && id != null) {
            return entityManager.createQuery("FROM Mention " +
                    "WHERE status = :status AND profile.username = :username AND type IN (:types) " +
                    "AND (createdAt < :createdAt OR (createdAt = :createdAt AND id < :id)) " +
                    "ORDER BY createdAt DESC, id DESC ", Mention.class)
                    .setParameter("status", MentionStatus.LINKED)
                    .setParameter("username", username)
                    .setParameter("types", types)
                    .setParameter("createdAt", new Date(createdAt))
                    .setParameter("id", id)
                    .setMaxResults(size)
                    .getResultList();
        }

        return entityManager.createQuery("FROM Mention " +
                "WHERE status = :status AND profile.username = :username AND type IN (:types) " +
                "ORDER BY createdAt DESC, id DESC ", Mention.class)
                .setParameter("status", MentionStatus.LINKED)
                .setParameter("username", username)
                .setParameter("types", types)
                .setMaxResults(size)
                .getResultList();
    }

    private List<Mention> selectByUsername(EntityManager entityManager, String username, int size, TransportCursor cursor) {
        final Long createdAt = cursor.getLong("createdAt");
        final String id = cursor.get("id");

        if (createdAt != null && id != null) {
            return entityManager.createQuery("SELECT m FROM Mention m " +
                    "WHERE m.status = :status AND m.profile.username = :username " +
                    "AND (createdAt < :createdAt OR (createdAt = :createdAt AND id < :id)) " +
                    "ORDER BY createdAt DESC, id DESC ", Mention.class)
                    .setParameter("status", MentionStatus.LINKED)
                    .setParameter("username", username)
                    .setParameter("createdAt", new Date(createdAt))
                    .setParameter("id", id)
                    .setMaxResults(size)
                    .getResultList();
        }

        return entityManager.createQuery("SELECT m FROM Mention m " +
                "WHERE m.status = :status AND m.profile.username = :username " +
                "ORDER BY createdAt DESC, id DESC ", Mention.class)
                .setParameter("status", MentionStatus.LINKED)
                .setParameter("username", username)
                .setMaxResults(size)
                .getResultList();
    }

    public TransportList queryByMe(String accountId, int size, Set<MentionStatus> statuses, Set<MentionType> types, TransportCursor cursor) {
        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                return selectByMe(entityManager, accountId, size, statuses, types, cursor);
            }).cursor(size, (mention, builder) -> {
                builder.put("createdAt", mention.getCreatedAt().getTime());
                builder.put("id", mention.getId());
            }).asTransportList();
        });
    }

    private List<Mention> selectByMe(EntityManager entityManager, String accountId, int size, Set<MentionStatus> statuses, Set<MentionType> types, TransportCursor cursor) {
        QueryChain<Mention> chain = QueryChain.select(entityManager, "FROM Mention", Mention.class);
        chain.orderBy("createdAt DESC");
        chain.orderBy("id DESC");
        chain.size(size);

        Profile profile = Profile.findByAccountId(entityManager, accountId);
        if (profile == null) return List.of();
        chain.where("profile = :profile", "profile", profile);

        final Long createdAt = cursor.getLong("createdAt");
        final String id = cursor.get("id");
        if (createdAt != null && id != null) {
            chain.where("(createdAt < :createdAt OR (createdAt = :createdAt AND id < :id))",
                    "id", id, "createdAt", new Date(createdAt)
            );
        }

        if (!statuses.isEmpty()) {
            chain.where("type IN (:types)", "types", statuses);
        }

        if (!types.isEmpty()) {
            chain.where("status IN (:statues)", "statues", types);
        }

        return chain.asList();
    }

    public static class QueryChain<T> {
        private final EntityManager entityManager;
        private final String select;
        private final Class<T> clazz;
        private String where;
        private String orderBy;

        private int from;
        private int size;

        private Map<String, Object> parameters = new HashMap<>();

        private QueryChain(EntityManager entityManager, @Language("HQL") String select, Class<T> clazz) {
            this.entityManager = entityManager;
            this.select = select + " ";
            this.clazz = clazz;
        }

        public static <T> QueryChain<T> select(EntityManager entityManager, @Language("HQL") String select, Class<T> clazz) {
            return new QueryChain<>(entityManager, select, clazz);
        }

        public QueryChain<T> where(String ql) {
            if (where == null) {
                where = " WHERE " + ql;
            } else {
                where += " AND" + ql;
            }
            return this;
        }

        /**
         * @param ql    query language
         * @param name  parameter name
         * @param value parameter value
         * @return QueryChain instance for chaining
         */
        public QueryChain<T> where(String ql, String name, Object value) {
            if (where == null) {
                where = " WHERE " + ql;
            } else {
                where += " AND" + ql;
            }
            parameters.put(name, value);
            return this;
        }

        public QueryChain<T> where(String ql, String name1, Object value1, String name2, Object value2) {
            parameters.put(name2, value2);
            return where(ql, name1, value1);
        }

        public QueryChain<T> where(String ql, String name1, Object value1, String name2, Object value2, String name3, Object value3) {
            parameters.put(name3, value3);
            return where(ql, name1, value1, name2, value2);
        }

        public QueryChain<T> where(String ql, String name1, Object value1, String name2, Object value2, String name3, Object value3, String name4, Object value4) {
            parameters.put(name4, value4);
            return where(ql, name1, value1, name2, value2, name3, value3);
        }

        public QueryChain<T> orderBy(String ql) {
            if (orderBy == null) {
                orderBy = " ORDER BY " + ql;
            } else {
                orderBy += " ," + ql;
            }
            return this;
        }

        public QueryChain<T> from(int from) {
            this.from = from;
            return this;
        }

        public QueryChain<T> size(int size) {
            this.size = size;
            return this;
        }

        public TypedQuery<T> asQuery() {
            String ql = select;
            if (where != null) ql += where;
            if (orderBy != null) ql += orderBy;

            TypedQuery<T> query = entityManager.createQuery(ql, clazz);
            parameters.forEach(query::setParameter);

            query.setFirstResult(from);
            query.setMaxResults(size);
            return query;
        }

        public List<T> asList() {
            return asQuery().getResultList();
        }
    }
}
