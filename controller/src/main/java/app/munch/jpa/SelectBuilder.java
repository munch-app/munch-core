package app.munch.jpa;

import javax.persistence.criteria.Selection;

/**
 * @author Fuxing Loh
 * @since 2019-10-30 at 23:59
 */
public interface SelectBuilder<T, B extends QueryBuilder<T>> extends Builder<T, B> {
    default B select(Selection<?> selection) {
        return with(builder -> {
            builder.selections.add(selection);
        });
    }

    default B select(String... names) {
        return with(b -> {
            for (String name : names) {
                b.selections.add(b.root.get(name).alias(name));
            }
        });
    }

    default B join(String... names) {
        return with(b -> {
            for (String name : names) {
                b.selections.add(b.root.join(name));
            }
        });
    }
}
