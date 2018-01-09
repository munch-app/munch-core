package munch.collections;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 3:38 PM
 * Project: munch-core
 */
@Singleton
public final class LikedPlaceClient {
    private static final String COLLECTION_ID = "liked";

    private final CollectionPlaceClient collectionPlaceClient;

    @Inject
    public LikedPlaceClient(CollectionPlaceClient collectionPlaceClient) {
        this.collectionPlaceClient = collectionPlaceClient;
    }

    /**
     * @param userId  user id of person to check
     * @param placeId id of place to check
     * @return whether the user liked to place
     */
    public boolean isLiked(String userId, String placeId) {
        return collectionPlaceClient.isAdded(userId, COLLECTION_ID, placeId);
    }

    /**
     * Add place to liked
     *
     * @param userId  user id of person
     * @param placeId id of place
     */
    public void add(String userId, String placeId) {
        collectionPlaceClient.add(userId, COLLECTION_ID, placeId);
    }

    /**
     * Remove place from liked
     *
     * @param userId  user id of person
     * @param placeId id of place
     */
    public void remove(String userId, String placeId) {
        collectionPlaceClient.remove(userId, COLLECTION_ID, placeId);
    }

    /**
     * @param userId user id of person
     * @return number of place in collection
     */
    public long count(String userId) {
        return collectionPlaceClient.count(userId, COLLECTION_ID);
    }

    /**
     * @param userId     user id of person
     * @param maxPlaceId max id of place, don't show result after this id
     * @param size       size per query
     * @return List of Place liked by the user
     */
    public List<AddedPlace> list(String userId, @Nullable String maxPlaceId, int size) {
        return collectionPlaceClient.list(userId, COLLECTION_ID, maxPlaceId, size);
    }
}
