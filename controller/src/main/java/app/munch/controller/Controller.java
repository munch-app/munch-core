package app.munch.controller;

import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;

/**
 * Date: 28/9/19
 * Time: 10:45 pm
 *
 * @author Fuxing Loh
 */
public abstract class Controller {
    protected TransactionProvider provider;

    @Inject
    protected void inject(TransactionProvider provider) {
        this.provider = provider;
    }
}
