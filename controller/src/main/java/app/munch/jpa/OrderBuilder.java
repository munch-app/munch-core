package app.munch.jpa;

import javax.persistence.criteria.Order;

/**
 * @author Fuxing Loh
 * @since 2019-10-31 at 00:10
 */
public interface OrderBuilder<T, B extends QueryBuilder<T>> extends Builder<T, B> {
    private B order(Order order) {
        return with(builder -> {
            builder.orders.add(order);
        });
    }

    default B orderAsc(String name) {
        return with(builder -> {
            builder.criteria.asc(builder.root.get(name));
        });
    }

    default B orderDesc(String name) {
        return with(builder -> {
            builder.criteria.desc(builder.root.get(name));
        });
    }
}
