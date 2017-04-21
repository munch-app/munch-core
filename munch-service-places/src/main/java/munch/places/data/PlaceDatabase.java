package munch.places.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceDatabase {

    private final TransactionProvider provider;

    @Inject
    public PlaceDatabase(TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param key key of place
     * @return Place with they key, nullable
     */
    public Place get(String key) {
        return provider.optional(em -> em
                .find(PostgresPlace.class, key))
                .map(PostgresPlace::getPlace)
                .orElse(null);
    }

    /**
     * @param keys list of key to query of a place
     * @return List of Place, non null values
     */
    public List<Place> get(List<String> keys) {
        if (keys.isEmpty()) return Collections.emptyList();
        List<PostgresPlace> list = provider.reduce(em -> em.createQuery("SELECT e FROM PostgresPlace e " +
                "WHERE e.id IN (:keys)", PostgresPlace.class)
                .setParameter("keys", keys)
                .getResultList());
        return list.stream().map(PostgresPlace::getPlace).collect(Collectors.toList());
    }

    /**
     * @param place place to put to database
     */
    public void put(Place place) {
        provider.with(em -> persist(em, place));
    }

    /**
     * @param places list of place to put to database
     */
    public void put(List<Place> places) {
        if (places.isEmpty()) return;
        provider.with(em -> places.forEach(place -> persist(em, place)));
    }

    /**
     * @param key of place to delete
     */
    public boolean delete(String key) {
        return provider.reduce(em -> em
                .createQuery("DELETE FROM PostgresPlace WHERE id = :id")
                .setParameter("id", key)
                .executeUpdate()
        ) != 0;
    }

    /**
     * @param keys list of key of place to delete
     */
    public boolean delete(List<String> keys) {
        if (keys.isEmpty()) return true;
        return provider.reduce(em -> em
                .createQuery("DELETE FROM PostgresPlace WHERE id IN (:keys)")
                .setParameter("keys", keys)
                .executeUpdate()
        ) != 0;
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
