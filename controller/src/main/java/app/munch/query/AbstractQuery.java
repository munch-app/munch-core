package app.munch.query;

import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;

/**
 * For complex query
 *
 * @author Fuxing Loh
 * @since 2019-10-09 at 12:25 am
 */
public abstract class AbstractQuery {
    protected TransactionProvider provider;

    @Inject
    protected void inject(TransactionProvider provider) {
        this.provider = provider;
    }
}
