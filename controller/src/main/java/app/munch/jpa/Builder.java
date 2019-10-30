package app.munch.jpa;

import java.util.function.Consumer;

/**
 * @author Fuxing Loh
 * @since 2019-10-31 at 00:12
 */
public interface Builder<T, Builder extends QueryBuilder<T>> {
    Builder with(Consumer<Builder> consumer);
}
