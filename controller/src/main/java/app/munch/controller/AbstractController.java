package app.munch.controller;

import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;

/**
 * When there are too many rules and it involve complex data lifecycle,
 * a Controller should be created to help facilitate and better understand the modularity of the entity.
 * <p>
 * Controller created here should not accept 'jpa.EntityManager'.
 * The controller itself should procure their own transaction and close them within it's own lifecycle.
 * <p>
 *
 * @author Fuxing Loh
 * @since 2019-09-28 at 23:23
 */
public abstract class AbstractController {
    protected TransactionProvider provider;

    @Inject
    protected void inject(TransactionProvider provider) {
        this.provider = provider;
    }
}
