package munch.collections;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 3:38 PM
 * Project: munch-core
 */
@Singleton
public final class CollectionClient {
    // TODO Actual

    public void put(PlaceCollection collection) {

    }

    public void delete(String userId, String collectionId) {

    }

    public PlaceCollection get(String userId, String collectionId) {
        return null;
    }

    public List<PlaceCollection> list(String userId, String maxCollectionId, int size) {
        return Collections.emptyList();
    }
}
