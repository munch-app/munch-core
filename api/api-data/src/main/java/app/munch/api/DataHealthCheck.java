package app.munch.api;

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

    /**
     * This method will be called multiple times for check.
     * Because checks can come from multiple availability zone.
     *
     * @throws Exception if any error in health check
     */
    @Override
    @SuppressWarnings("SqlNoDataSourceInspection")
    public void check() throws Exception {
        provider.with(em -> {
            em.createNativeQuery("SELECT 'h'")
                    .getSingleResult();
        });
    }
}

