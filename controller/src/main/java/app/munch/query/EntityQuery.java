package app.munch.query;

import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import org.intellij.lang.annotations.Language;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Date: 1/10/19
 * Time: 2:33 pm
 *
 * @author Fuxing Loh
 */
public class EntityQuery<T> {
    // TODO(fuxing): Move this to transport-java when it's stable

    private final EntityManager entityManager;
    private final String select;
    private final Class<T> clazz;
    private String where;
    private String orderBy;

    private int from = 0;
    private int size = 30;

    private Map<String, Object> parameters = new HashMap<>();

    private EntityQuery(EntityManager entityManager, @Language("HQL") String select, Class<T> clazz) {
        this.entityManager = entityManager;
        this.select = select + " ";
        this.clazz = clazz;
    }

    public static <T> EntityQuery<T> select(EntityManager entityManager, @Language("HQL") String select, Class<T> clazz) {
        return new EntityQuery<>(entityManager, select, clazz);
    }

    public EntityQuery<T> where(String ql) {
        if (where == null) {
            where = " WHERE " + ql;
        } else {
            where += " AND " + ql;
        }
        return this;
    }

    public EntityQuery<T> where(String name, Object value) {
        String ql = name + " = :" + name;
        return where(ql, name, value);
    }

    /**
     * @param ql    query language
     * @param name  parameter name
     * @param value parameter value
     * @return QueryChain instance for chaining
     */
    public EntityQuery<T> where(String ql, String name, Object value) {
        if (where == null) {
            where = " WHERE " + ql;
        } else {
            where += " AND " + ql;
        }
        parameters.put(name, value);
        return this;
    }

    public EntityQuery<T> where(String ql, String name1, Object value1, String name2, Object value2) {
        parameters.put(name2, value2);
        return where(ql, name1, value1);
    }

    public EntityQuery<T> where(String ql, String name1, Object value1, String name2, Object value2, String name3, Object value3) {
        parameters.put(name3, value3);
        return where(ql, name1, value1, name2, value2);
    }

    public EntityQuery<T> where(String ql, String name1, Object value1, String name2, Object value2, String name3, Object value3, String name4, Object value4) {
        parameters.put(name4, value4);
        return where(ql, name1, value1, name2, value2, name3, value3);
    }

    /**
     * @param predicate before running consumer
     * @param consumer  to run if predicate pass
     * @return QueryChain instance for chaining
     */
    public EntityQuery<T> predicate(boolean predicate, Consumer<EntityQuery<T>> consumer) {
        if (predicate) {
            consumer.accept(this);
        }
        return this;
    }

    /**
     * @param consumer for functional chaining
     * @return QueryChain instance for chaining
     */
    public EntityQuery<T> consume(Consumer<EntityQuery<T>> consumer) {
        consumer.accept(this);
        return this;
    }

    public EntityQuery<T> orderBy(String ql) {
        if (orderBy == null) {
            orderBy = " ORDER BY " + ql;
        } else {
            orderBy += " ," + ql;
        }
        return this;
    }

    public EntityQuery<T> from(int from) {
        this.from = from;
        return this;
    }

    /**
     * @param size to query, will be used for created TransportList in EntitySteam
     * @return QueryChain instance for chaining
     * @see EntityStream#cursor(int, BiConsumer)
     */
    public EntityQuery<T> size(int size) {
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

    public EntityStream asStream() {
        return new EntityStream(asList());
    }

    public TransportList asTransportList(BiConsumer<T, TransportCursor.Builder> consumer) {
        EntityStream stream = asStream();
        stream.cursor(consumer);
        return stream.asTransportList();
    }

    public class EntityStream extends dev.fuxing.jpa.EntityStream<T> {
        public EntityStream(List<T> list) {
            super(list, null);
        }

        public EntityStream cursor(BiConsumer<T, TransportCursor.Builder> consumer) {
            super.cursor(size, consumer);
            return this;
        }

        public TransportList asTransportList(BiConsumer<T, TransportCursor.Builder> consumer) {
            cursor(consumer);
            return asTransportList();
        }

        @Override
        public EntityStream peek(Consumer<T> consumer) {
            super.peek(consumer);
            return this;
        }
    }
}
