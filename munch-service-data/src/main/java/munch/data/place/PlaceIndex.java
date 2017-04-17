package munch.data.place;

import com.google.inject.Inject;
import com.munch.hibernate.utils.TransactionProvider;

import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 9:58 PM
 * Project: munch-core
 */
@Singleton
public class PlaceIndex {

    private final TransactionProvider provider;

    @Inject
    public PlaceIndex(TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @return latest data in postgres place index
     */
    public PostgresPlace latest() {
        return provider.optional(em -> em.createQuery("SELECT p FROM PostgresPlace p " +
                "ORDER BY p.updatedDate DESC, p.id DESC", PostgresPlace.class)
                .setMaxResults(1).getSingleResult())
                .orElse(null);
    }
}
