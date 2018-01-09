package munch.collections;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 4:02 PM
 * Project: munch-core
 */
@Singleton
public final class CollectionPlaceClient {
    // TODO Actual

    public boolean isAdded(String userId, String collectionId, String placeId) {
        return true;
    }

    public void add(String userId, String collectionId, String placeId) {

    }

    public void remove(String userId, String collectionId, String placeId) {

    }

    public long count(String userId, String collectionId) {
        return 0;
    }

    public List<AddedPlace> list(String userId, String collectionId, String maxPlaceId, int size) {
        return null;
    }
}
