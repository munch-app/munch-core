package com.munch.struct.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.hibernate.utils.TransactionProvider;
import com.munch.struct.Place;
import com.munch.struct.EntityPlace;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 2:39 PM
 * Project: munch-core
 */
@Singleton
public class PostgresDocumentDatabase implements DocumentDatabase {

    private final TransactionProvider provider;

    @Inject
    public PostgresDocumentDatabase(@Named("struct") TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param em    entity manager
     * @param place place to persist
     * @throws NullPointerException if place or place id is null
     */
    private void persist(EntityManager em, Place place) {
        String key = Objects.requireNonNull(place.getId(), "Place id cannot be null");

        EntityPlace entity = em.find(EntityPlace.class, key);
        if (entity == null) entity = new EntityPlace(key);

        entity.setPlace(place);
        entity.setCreatedDate(place.getCreatedDate());
        entity.setUpdatedDate(place.getUpdatedDate());
        em.persist(entity);
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
        provider.with(em -> places.forEach(place -> persist(em, place)));
    }

    /**
     * @param key of place to delete
     */
    public boolean delete(String key) {
        return provider.reduce(em -> em.createQuery("DELETE FROM EntityPlace e " +
                "WHERE e.id = :id").setParameter("id", key)
                .executeUpdate()) != 0;
    }

    /**
     * @param keys list of key of place to delete
     */
    public boolean delete(List<String> keys) {
        return provider.reduce(em -> em.createQuery("DELETE FROM EntityPlace e " +
                "WHERE e.id IN :keys").setParameter("keys", keys)
                .executeUpdate()) != 0;
    }

    /**
     * @param key key of place
     * @return Place with they key, nullable
     */
    public Place get(String key) {
        EntityPlace entity = provider.reduce(em -> em.find(EntityPlace.class, key));
        return entity == null ? null : entity.getPlace();
    }

    /**
     * @param keys list of key to query of a place
     * @return List of Place, non null values
     */
    public List<Place> get(List<String> keys) {
        if (keys.isEmpty()) return Collections.emptyList();
        List<EntityPlace> list = provider.reduce(em -> em.createQuery("SELECT e FROM EntityPlace e " +
                "WHERE e.id IN :keys", EntityPlace.class).getResultList());
        return list.stream().map(EntityPlace::getPlace).collect(Collectors.toList());
    }
}
