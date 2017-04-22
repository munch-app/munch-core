package munch.places.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.RollbackException;
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
public class ImageLinkDatabase {

    private final TransactionProvider provider;

    @Inject
    public ImageLinkDatabase(TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param placeId associated place id
     * @param from    start from where
     * @param size    size of list
     * @return List of ImageLink
     */
    public List<ImageLink> list(String placeId, int from, int size) {
        return provider.reduce(em -> em.createQuery("FROM ImageLink WHERE " +
                "placeId = :placeId ORDER BY createdDate DESC", ImageLink.class)
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
        ImageLink link = new ImageLink();
        link.setPlaceId(placeId);
        link.setImageKey(imageKey);

        link.setSourceName(sourceName);
        link.setSourceId(sourceId);
        link.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        try {
            provider.with(em -> em.persist(link));
        } catch (RollbackException e) {
            ExceptionUtils.getThrowableList(e).stream()
                    .filter(t -> t instanceof ConstraintViolationException)
                    .map(t -> (ConstraintViolationException) t)
                    .filter(c -> c.getConstraintName().equals(ImageLink.UNIQUE_CONSTRAINT_IMAGE_KEY))
                    .findAny()
                    .orElseThrow(() -> e);
        }
    }

    /**
     * @param imageKeys collection of image key to delete
     */
    public void delete(Collection<String> imageKeys) {
        if (imageKeys.isEmpty()) return;

        provider.with(em -> em.createQuery("DELETE FROM ImageLink WHERE imageKey IN (:keys)")
                .setParameter("keys", imageKeys)
                .executeUpdate());
    }
}
