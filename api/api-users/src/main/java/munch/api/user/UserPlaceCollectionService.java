package munch.api.user;

import munch.api.ApiService;
import munch.restful.core.NextNodeList;
import munch.restful.core.exception.CodeException;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.UserPlaceCollectionClient;
import munch.user.data.UserPlaceCollection;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 8/6/18
 * Time: 2:54 PM
 * Project: munch-core
 */
@Singleton
public final class UserPlaceCollectionService extends ApiService {

    private final UserPlaceCollectionClient collectionClient;

    @Inject
    public UserPlaceCollectionService(UserPlaceCollectionClient collectionClient) {
        this.collectionClient = collectionClient;
    }

    @Override
    public void route() {
        PATH("/users/places/collections", () -> {
            GET("", this::list);
            GET("/:collectionId", this::get);

            POST("", this::post);
            PATCH("/:collectionId", this::patch);
            DELETE("/:collectionId", this::delete);


            PATH("/:collectionId/items", () -> {
                GET("", this::listItems);
                PUT("/:placeId", this::putItem);
                DELETE("/:placeId", this::deleteItem);
            });
        });
    }

    public JsonResult list(JsonCall call) {
        String userId = getUserId(call);
        Long sort = call.queryObject("next.sort", null, Long.class);
        int size = call.querySize(20, 20);

        NextNodeList<UserPlaceCollection> nodeList = collectionClient.list(userId, sort, size);
        JsonResult result = JsonResult.ok(nodeList);

        if (nodeList.hasNext()) result.put("next", nodeList.getNext());
        return result;
    }

    public UserPlaceCollection get(JsonCall call) {
        String collectionId = call.pathString("collectionId");

        UserPlaceCollection collection = collectionClient.get(collectionId);
        return reduceCollection(collection, optionalUserId(call).orElse(null));
    }

    public UserPlaceCollection post(JsonCall call) {
        String userId = getUserId(call);

        UserPlaceCollection collection = call.bodyAsObject(UserPlaceCollection.class);
        collection.setUserId(userId);

        return collectionClient.post(collection);
    }

    public UserPlaceCollection patch(JsonCall call) {
        String userId = getUserId(call);
        String collectionId = call.pathString("collectionId");

        UserPlaceCollection collection = collectionClient.get(collectionId);
        validateUser(collection, userId);

        return collectionClient.patch(collectionId, call.bodyAsJson());
    }

    public UserPlaceCollection delete(JsonCall call) {
        String userId = getUserId(call);
        String collectionId = call.pathString("collectionId");

        UserPlaceCollection collection = collectionClient.get(collectionId);
        validateUser(collection, userId);

        return collectionClient.delete(collectionId);
    }

    public JsonResult listItems(JsonCall call) {
        String collectionId = call.pathString("collectionId");
        Long sort = call.queryObject("next.sort", null, Long.class);
        int size = call.querySize(20, 40);

        NextNodeList<UserPlaceCollection.Item> nodeList = collectionClient.listItems(collectionId, sort, size);
        JsonResult result = JsonResult.ok(nodeList);

        if (nodeList.hasNext()) result.put("next", nodeList.getNext());
        return result;
    }

    public JsonResult putItem(JsonCall call) throws ItemAlreadyExistInPlaceCollection {
        String userId = getUserId(call);
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        UserPlaceCollection collection = collectionClient.get(collectionId);
        validateUser(collection, userId);

        UserPlaceCollection.Item item = collectionClient.getItem(collectionId, placeId);
        if (item != null) {
            throw new ItemAlreadyExistInPlaceCollection(placeId);
        }

        collectionClient.addItem(collectionId, placeId, new UserPlaceCollection.Item());
        return result(200);
    }

    public UserPlaceCollection.Item deleteItem(JsonCall call) {
        String userId = getUserId(call);
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        UserPlaceCollection collection = collectionClient.get(collectionId);
        validateUser(collection, userId);

        return collectionClient.deleteItem(collectionId, placeId);
    }

    private static void validateUser(UserPlaceCollection collection, String userId) {
        if (collection == null) throw new CodeException(404);
        if (!collection.getUserId().equals(userId)) throw new CodeException(503);
    }

    private static UserPlaceCollection reduceCollection(UserPlaceCollection collection, @Nullable String userId) {
        if (collection == null) return null;
        if (collection.getUserId().equals(userId)) return collection;

        if (collection.getVisibility() == UserPlaceCollection.Visibility.anyone) return collection;
        return null;
    }
}