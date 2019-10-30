package app.munch.jpa;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.utils.JsonUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Fuxing Loh
 * @since 2019-10-30 at 08:22
 */
public class QueryBuilder<T> implements
        SelectBuilder<T, QueryBuilder<T>>,
        WhereBuilder<T, QueryBuilder<T>>,
        OrderBuilder<T, QueryBuilder<T>>,
        CursorBuilder<T, QueryBuilder<T>> {
    protected final EntityManager entityManager;

    public final CriteriaBuilder criteria;
    public final CriteriaQuery<Tuple> query;
    public final Root<T> root;
    protected final Class<T> clazz;

    public final List<Predicate> predicates = new ArrayList<>();
    public final List<Selection<?>> selections = new ArrayList<>();
    public final List<Order> orders = new ArrayList<>();

    protected int first = 0;
    protected int max = 30;

    private QueryBuilder(Class<T> clazz, EntityManager entityManager) {
        this.clazz = clazz;
        this.entityManager = entityManager;
        this.criteria = entityManager.getCriteriaBuilder();
        this.query = criteria.createTupleQuery();
        this.root = query.from(clazz);
    }

    public static <T> QueryBuilder<T> from(Class<T> clazz, EntityManager entityManager) {
        return new QueryBuilder<>(clazz, entityManager);
    }

    public QueryBuilder<T> with(boolean predicate, Consumer<QueryBuilder<T>> consumer) {
        if (predicate) {
            return with(consumer);
        }
        return this;
    }

    public QueryBuilder<T> with(Consumer<QueryBuilder<T>> consumer) {
        consumer.accept(this);
        return this;
    }

    public QueryBuilder<T> first(int first) {
        this.first = first;
        return this;
    }

    public QueryBuilder<T> max(int size) {
        this.max = size;
        return this;
    }

    public List<JsonNode> asJson() {
        CriteriaQuery<Tuple> query = this.query
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(orders);

        if (selections.isEmpty()) {
            query = query.select(criteria.tuple(root).alias("data"));
        } else {
            query = query.multiselect(selections);
        }

        List<Tuple> results = entityManager.createQuery(query)
                .setFirstResult(first)
                .setMaxResults(max)
                .getResultList();

        if (selections.isEmpty()) {
            return results.stream()
                    .map(tuple -> {
                        T data = tuple.get("data", clazz);
                        return (JsonNode) JsonUtils.valueToTree(data);
                    })
                    .collect(Collectors.toList());
        } else {
            return results.stream()
                    .map(tuple -> {
                        ObjectNode node = JsonUtils.createObjectNode();
                        tuple.getElements().forEach(element -> {
                            String name = element.getAlias();
                            Object object = tuple.get(name);
                            node.set(name, JsonUtils.valueToTree(object));
                        });
                        return node;
                    })
                    .collect(Collectors.toList());
        }
    }

    public List<T> asList() {
        return asJson().stream()
                .map(node -> JsonUtils.toObject(node, clazz))
                .collect(Collectors.toList());
    }

    public EntityStream<T> asStream(BiConsumer<T, TransportCursor.Builder> consumer) {
        return EntityStream.of(this::asList)
                .cursor(max, consumer);
    }

    public TransportList asTransportList(BiConsumer<T, TransportCursor.Builder> consumer) {
        return asStream(consumer).asTransportList();
    }
}
