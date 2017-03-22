package munch.document;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.hibernate.utils.TransactionProvider;
import munch.struct.Place;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 2:39 PM
 * Project: munch-core
 */
@Singleton
public class PostgresStore implements DocumentStore {

    private final TransactionProvider provider;

    @Inject
    public PostgresStore(@Named("struct") TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param place place to put to database
     */
    @Override
    public void put(Place place) {
        provider.with(em -> persist(em, place));
    }

    /**
     * @param places list of place to put to database
     */
    @Override
    public void put(List<Place> places) {
        if (places.isEmpty()) return;
        provider.with(em -> places.forEach(place -> persist(em, place)));
    }

    /**
     * @param key of place to delete
     */
    @Override
    public boolean delete(String key) {
        return provider.reduce(em -> em.createQuery("DELETE FROM PostgresPlace e " +
                "WHERE e.id = :id").setParameter("id", key)
                .executeUpdate()) != 0;
    }

    /**
     * @param keys list of key of place to delete
     */
    @Override
    public boolean delete(List<String> keys) {
        if (keys.isEmpty()) return true;
        return provider.reduce(em -> em.createQuery("DELETE FROM PostgresPlace e " +
                "WHERE e.id IN (:keys)").setParameter("keys", keys)
                .executeUpdate()) != 0;
    }

    /**
     * @param em    entity manager
     * @param place place to persist
     * @throws NullPointerException if place or place id is null
     */
    private static void persist(EntityManager em, Place place) {
        String key = Objects.requireNonNull(place.getId(), "Place id cannot be null");

        PostgresPlace entity = em.find(PostgresPlace.class, key);
        if (entity == null) entity = new PostgresPlace(key);

        entity.setPlace(place);
        entity.setCreatedDate(place.getCreatedDate());
        entity.setUpdatedDate(place.getUpdatedDate());
        em.persist(entity);
    }
}
