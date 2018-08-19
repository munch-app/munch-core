package munch.api.user;

import munch.restful.core.JsonUtils;
import munch.restful.core.NextNodeList;
import munch.user.client.UserPlaceCollectionClient;
import munch.user.data.UserPlaceCollection;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/8/18
 * Time: 9:28 PM
 * Project: munch-core
 */
@Singleton
public final class DefaultUserPlaceCollection {
    private final UserPlaceCollectionClient collectionClient;

    @Inject
    public DefaultUserPlaceCollection(UserPlaceCollectionClient collectionClient) {
        this.collectionClient = collectionClient;
    }

    /**
     * @return 2 Newly create default UserPlaceCollection for User
     */
    public NextNodeList<UserPlaceCollection> create(String userId) {
        UserPlaceCollection saved = createSaved(userId);
        UserPlaceCollection favourites = createFavourite(userId);
        return new NextNodeList<>(List.of(favourites, saved), JsonUtils.createObjectNode());
    }

    private UserPlaceCollection createSaved(String userId) {
        UserPlaceCollection collection = new UserPlaceCollection();
        collection.setUserId(userId);
        collection.setAccess(UserPlaceCollection.Access.Public);
        collection.setCreatedBy(UserPlaceCollection.CreatedBy.Default);
        collection.setName("Saved for later");
        collection.setDescription("There’s always next time");
        collection.setSort(Long.MAX_VALUE);
        collection.setImage(null);
        return collectionClient.post(collection);
    }

    private UserPlaceCollection createFavourite(String userId) {
        UserPlaceCollection collection = new UserPlaceCollection();
        collection.setUserId(userId);
        collection.setAccess(UserPlaceCollection.Access.Public);
        collection.setCreatedBy(UserPlaceCollection.CreatedBy.Default);
        collection.setName("Favourites");
        collection.setDescription("Your top spots");
        collection.setSort(Long.MAX_VALUE - 1);
        collection.setImage(null);
        return collectionClient.post(collection);
    }
}
