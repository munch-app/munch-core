package munch.places.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.places.PlaceNotFound;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 6:35 PM
 * Project: munch-core
 */
@Singleton
public class ImageDatabase {

    private final TransactionProvider provider;

    @Inject
    public ImageDatabase(TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param placeId associated place id
     * @param from    start from where
     * @param size    size of list
     * @return List of ImageLink
     */
    public List<LinkedImage> list(String placeId, int from, int size) {
        return provider.reduce(em -> em.createQuery("FROM LinkedImage WHERE " +
                "place.id = :placeId ORDER BY createdDate DESC", LinkedImage.class)
                .setParameter("placeId", placeId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList());
    }

    /**
     * @param placeId    associated place id
     * @param imageKey   image key in munch-images
     * @param sourceName source/provider of image
     * @param sourceId   source/provider of image id
     */
    public void put(String placeId, String imageKey, String sourceName, String sourceId) {
        LinkedImage link = new LinkedImage();
        link.setImageKey(imageKey);
        link.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        link.setSourceName(sourceName);
        link.setSourceId(sourceId);

        provider.with(em -> {
            Place place = em.find(Place.class, placeId);
            if (place == null) throw new PlaceNotFound(placeId);
            link.setPlace(place);
            em.persist(link);
        });
    }

    /**
     * @param imageKeys collection of image key to delete
     */
    public void delete(Collection<String> imageKeys) {
        if (imageKeys.isEmpty()) return;

        provider.with(em -> em.createQuery("DELETE FROM LinkedImage WHERE imageKey IN (:keys)")
                .setParameter("keys", imageKeys)
                .executeUpdate());
    }
}
