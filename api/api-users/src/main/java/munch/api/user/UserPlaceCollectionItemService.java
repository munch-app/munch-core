package munch.api.user;

import munch.api.ApiRequest;
import munch.api.ApiService;
import munch.api.user.collection.ItemAlreadyExistInPlaceCollection;
import munch.data.client.PlaceClient;
import munch.data.place.Place;
import munch.restful.core.NextNodeList;
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
public final class UserPlaceCollectionItemService extends ApiService {
    private final UserPlaceCollectionClient collectionClient;
    private final PlaceClient placeClient;

    @Inject
    public UserPlaceCollectionItemService(UserPlaceCollectionClient collectionClient, PlaceClient placeClient) {
        this.collectionClient = collectionClient;
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        PATH("/users/places/collections/:collectionId/items", () -> {
            GET("", this::list);
            GET("/:placeId", this::get);
            PUT("/:placeId", this::put);
            DELETE("/:placeId", this::delete);
        });
    }

    public JsonResult list(JsonCall call) {
        String collectionId = call.pathString("collectionId");
        Long sort = call.queryObject("next.sort", null, Long.class);
        int size = call.querySize(20, 40);

        NextNodeList<UserPlaceCollection.Item> nextList = collectionClient.listItems(collectionId, sort, size);
        List<Item> items = placeClient.batchGet(nextList, UserPlaceCollection.Item::getPlaceId, (item, place) -> {
            if (place != null) return new Item(item, place);

            // Delete Item saved in Collection if it don't exist anymore
//            collectionClient.deleteItem(collectionId, item.getPlaceId());
            return null;
        });

        JsonResult result = JsonResult.ok(items);
        if (nextList.hasNext()) result.put("next", nextList.getNext());
        return result;
    }

    public Item get(JsonCall call) {
        call.get(ApiRequest.class).getUserId();
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        UserPlaceCollection.Item item = collectionClient.getItem(collectionId, placeId);
        if (item == null) return null;
        Place place = placeClient.get(placeId);
        if (place != null) return new Item(item, place);

        // Delete Item saved in Collection if it don't exist anymore
//        collectionClient.deleteItem(collectionId, placeId);
        return null;
    }

    public Item put(JsonCall call) throws ItemAlreadyExistInPlaceCollection {
        String userId = call.get(ApiRequest.class).getUserId();
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        UserPlaceCollectionService.validateAccess(collectionClient.get(collectionId), userId, UserPlaceCollection.CreatedBy.Default);

        UserPlaceCollection.Item item = collectionClient.getItem(collectionId, placeId);
        if (item != null) throw new ItemAlreadyExistInPlaceCollection(placeId);

        item = collectionClient.addItem(collectionId, placeId, new UserPlaceCollection.Item());
        Place place = placeClient.get(placeId);
        return new Item(item, place);
    }

    public UserPlaceCollection.Item delete(JsonCall call) {
        String userId = call.get(ApiRequest.class).getUserId();
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        UserPlaceCollectionService.validateAccess(collectionClient.get(collectionId), userId, UserPlaceCollection.CreatedBy.Default);

        return collectionClient.deleteItem(collectionId, placeId);
    }

    public static final class Item {
        public String collectionId;
        public String placeId;

        public Long sort;
        public Long createdMillis;
        public Place place;

        public Item(UserPlaceCollection.Item item, Place place) {
            this.collectionId = item.getCollectionId();
            this.placeId = item.getPlaceId();
            this.sort = item.getSort();
            this.createdMillis = item.getCreatedMillis();
            this.place = place;
        }
    }
}