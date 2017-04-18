package munch.places;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.places.data.PostgresPlace;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 4:21 PM
 * Project: munch-core
 */
@Singleton
public class MetaService implements JsonService {

    private final TransactionProvider provider;

    @Inject
    public MetaService(TransactionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void route() {
        PATH("/places/meta", () -> {
            GET("/last", this::last);
        });
    }

    /**
     * @param call json call
     * @return postgres place data
     */
    private PostgresPlace last(JsonCall call) {
        return provider.optional(em -> em
                .createQuery("FROM PostgresPlace p ORDER BY updatedDate DESC, id DESC",
                        PostgresPlace.class)
                .setMaxResults(1)
                .getSingleResult()
        ).orElse(null);
    }
}
