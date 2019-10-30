package app.munch.jpa;

import javax.persistence.criteria.Predicate;

/**
 * @author Fuxing Loh
 * @since 2019-10-31 at 00:30
 */
public interface WhereBuilder<T, B extends QueryBuilder<T>> extends Builder<T, B> {
    default B where(Predicate predicate) {
        return with(b -> {
            b.predicates.add(predicate);
        });
    }

    default B where(String name, Object value) {
        return with(b -> {
            b.predicates.add(b.criteria.equal(b.root.get(name), value));
        });
    }

    default B whereNotEqual(String name, Object value) {
        return with(b -> {
            b.predicates.add(b.criteria.notEqual(b.root.get(name), value));
        });
    }


}
