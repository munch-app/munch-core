package app.munch.api;

import app.munch.model.Profile;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-08-14
 * Time: 09:04
 */
@Singleton
public final class DataHealthCheck extends ApiHealthCheck {

    private final TransactionProvider provider;

    @Inject
    public DataHealthCheck(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void check() throws Exception {
        provider.with(entityManager -> {
            entityManager.find(Profile.class, Profile.ADMIN_ID);
        });
    }
}
