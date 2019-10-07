package app.munch.controller;

import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import org.intellij.lang.annotations.Language;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Date: 1/10/19
 * Time: 2:33 pm
 *
 * @author Fuxing Loh
 */
public class QueryChain<T> {
    // TODO(fuxing): Move this to transport-java when it's stable

    private final EntityManager entityManager;
    private final String select;
    private final Class<T> clazz;
    private String where;
    private String orderBy;

    private int from = 0;
    private int size = 30;

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

    /**
     * @param cursor    to chain
     * @param predicate before running consumer
     * @param consumer  to run if predicate pass
     * @return QueryChain instance for chaining
     */
    public QueryChain<T> cursor(TransportCursor cursor, Predicate<TransportCursor> predicate, BiConsumer<QueryChain<T>, TransportCursor> consumer) {
        if (predicate.test(cursor)) {
            consumer.accept(this, cursor);
        }
        return this;
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

    /**
     * @param size to query, will be used for created TransportList in EntitySteam
     * @return QueryChain instance for chaining
     * @see EntityStream#cursor(int, BiConsumer)
     */
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

    public EntityStream<T> asStream() {
        return EntityStream.of(asList());
    }

    public TransportList asTransportList(BiConsumer<T, TransportCursor.Builder> consumer) {
        EntityStream<T> stream = EntityStream.of(asList());
        stream.cursor(size, consumer);
        return stream.asTransportList();
    }
}
