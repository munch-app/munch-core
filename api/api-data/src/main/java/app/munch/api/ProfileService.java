package app.munch.api;

import app.munch.model.Profile;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:46
 */
@Singleton
public final class ProfileService extends DataService {

    @Inject
    ProfileService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/profiles", () -> {
            GET("/:username", this::get);
        });
    }

    public Profile get(TransportContext ctx) {
        String username = ctx.pathString("username");

        return provider.reduce(true, entityManager -> {
            return entityManager.createQuery("FROM Profile WHERE username = :username", Profile.class)
                    .setParameter("username", username)
                    .getSingleResult();
        });
    }
}
