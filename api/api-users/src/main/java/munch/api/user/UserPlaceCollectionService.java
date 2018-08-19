package munch.api.user;

import munch.api.ApiService;
import munch.data.client.PlaceClient;
import munch.data.place.Place;
import munch.file.Image;
import munch.restful.core.NextNodeList;
import munch.restful.core.exception.CodeException;
import munch.restful.core.exception.ForbiddenException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserPlaceCollectionClient;
import munch.user.data.UserPlaceCollection;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 8/6/18
 * Time: 2:54 PM
 * Project: munch-core
 */
@Singleton
public final class UserPlaceCollectionService extends ApiService {

    private final UserPlaceCollectionClient collectionClient;
    private final DefaultUserPlaceCollection defaultUserPlaceCollection;
    private final PlaceClient placeClient;

    @Inject
    public UserPlaceCollectionService(UserPlaceCollectionClient collectionClient, DefaultUserPlaceCollection defaultUserPlaceCollection, PlaceClient placeClient) {
        this.collectionClient = collectionClient;
        this.defaultUserPlaceCollection = defaultUserPlaceCollection;
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        PATH("/users/places/collections", () -> {
            GET("", this::list);
            POST("", this::post);

            GET("/:collectionId", this::get);
            PATCH("/:collectionId", this::patch);
            DELETE("/:collectionId", this::delete);
        });
    }

    /**
     * @return List of UserPlaceCollection owned by current user
     */
    public JsonResult list(JsonCall call) {
        String userId = getUserId(call);
        Long sort = call.queryObject("next.sort", null, Long.class);
        int size = call.querySize(10, 20);

        NextNodeList<UserPlaceCollection> nodeList = collectionClient.list(userId, sort, size);

        // Create DefaultCollections if sort is 0 and nodeList is 0
        if (sort == null && size > 0 && nodeList.size() == 0) {
            nodeList = defaultUserPlaceCollection.create(userId);
            return JsonResult.ok(nodeList);
        }

        nodeList.forEach(this::resolveImage);

        JsonResult result = JsonResult.ok(nodeList);
        if (nodeList.hasNext()) result.put("next", nodeList.getNext());
        return result;
    }

    /**
     * Whether the user has access to view the entire collection
     * - Check whether user owns the collection = Always can view
     * - Check collection is public, Always can view
     *
     * @return the same UserPlaceCollection
     */
    public UserPlaceCollection get(JsonCall call) {
        String collectionId = call.pathString("collectionId");
        String userId = optionalUserId(call).orElse(null);

        UserPlaceCollection collection = collectionClient.get(collectionId);
        if (collection == null) return null;
        if (collection.getUserId().equals(userId)) return collection;
        if (collection.getAccess() == UserPlaceCollection.Access.Public) return collection;
        resolveImage(collection);
        return null;
    }

    /**
     * @param collection add image if don't exist
     */
    private void resolveImage(UserPlaceCollection collection) {
        if (collection.getImage() != null) return;

        for (UserPlaceCollection.Item item : collectionClient.listItems(collection.getCollectionId(), null, 10)) {
            Place place = placeClient.get(item.getPlaceId());
            if (place == null) continue;

            List<Image> images = place.getImages();
            if (!images.isEmpty()) {
                collection.setImage(images.get(0));
                return;
            }
        }
    }

    public UserPlaceCollection post(JsonCall call) {
        String userId = getUserId(call);

        UserPlaceCollection collection = call.bodyAsObject(UserPlaceCollection.class);
        collection.setCreatedBy(UserPlaceCollection.CreatedBy.User);
        collection.setUserId(userId);
        return collectionClient.post(collection);
    }

    public UserPlaceCollection patch(JsonCall call) {
        String userId = getUserId(call);
        String collectionId = call.pathString("collectionId");

        validateAccess(collectionClient.get(collectionId), userId);

        return collectionClient.patch(collectionId, call.bodyAsJson());
    }

    public UserPlaceCollection delete(JsonCall call) {
        String userId = getUserId(call);
        String collectionId = call.pathString("collectionId");

        validateAccess(collectionClient.get(collectionId), userId);

        return collectionClient.delete(collectionId);
    }

    /**
     * Whether user has the right to edit items in the User Place Collection.
     * - Check whether user owns the collection
     * - Check whether user can rights to edit collection, collection can be created by AI
     *
     * @param collection UserPlaceCollection
     * @param userId     user id operating the collection
     * @param ignores    CreatedBy to ignore when validating
     */
    static void validateAccess(UserPlaceCollection collection, String userId, UserPlaceCollection.CreatedBy... ignores) {
        if (collection == null) throw new CodeException(404);
        if (!collection.getUserId().equals(userId)) throw new CodeException(503);

        for (UserPlaceCollection.CreatedBy ignore : ignores) {
            if (collection.getCreatedBy() == ignore) return;
        }

        if (collection.getCreatedBy() != UserPlaceCollection.CreatedBy.User) {
            throw new ForbiddenException("createdBy != User prevent you from updating Collection.");
        }
    }
}