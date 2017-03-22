package munch.document;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.hibernate.utils.TransactionProvider;
import munch.struct.Place;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:25 PM
 * Project: munch-core
 */
@Singleton
public class PostgresQuery implements DocumentQuery {

    private final TransactionProvider provider;

    @Inject
    public PostgresQuery(@Named("struct") TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param key key of place
     * @return Place with they key, nullable
     */
    @Override
    public Place get(String key) {
        PostgresPlace entity = provider.reduce(em -> em.find(PostgresPlace.class, key));
        return entity == null ? null : entity.getPlace();
    }

    /**
     * @param keys list of key to query of a place
     * @return List of Place, non null values
     */
    @Override
    public List<Place> get(List<String> keys) {
        if (keys.isEmpty()) return Collections.emptyList();
        List<PostgresPlace> list = provider.reduce(em -> em.createQuery("SELECT e FROM PostgresPlace e " +
                "WHERE e.id IN (:keys)", PostgresPlace.class).getResultList());
        return list.stream().map(PostgresPlace::getPlace).collect(Collectors.toList());
    }
}
