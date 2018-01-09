package munch.collections;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 3:38 PM
 * Project: munch-core
 */
@Singleton
public final class CollectionClient {
    private static final String DYNAMO_TABLE_NAME = "munch-core.PlaceCollection";

    private final Table table;
    private final Index sortIndex;

    @Inject
    public CollectionClient(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable(DYNAMO_TABLE_NAME);
        this.sortIndex = table.getIndex("sortKey-index");
    }

    public void put(PlaceCollection collection) {
        Objects.requireNonNull(collection.getUserId());
        Objects.requireNonNull(collection.getName());

        // TODO Inject CollectionId
        // TODO Inject SortKey
        // TODO Inject createdDate
        // TODO Update updatedDate


        // TODO Validate name
        // TODO Validate description
    }

    public void delete(String userId, String collectionId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(collectionId);
        table.deleteItem("userId", userId, "collectionId", collectionId);
    }

    public PlaceCollection get(String userId, String collectionId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(collectionId);

        Item item = table.getItem("userId", userId, "collectionId", collectionId);
        if (item == null) return null;
        return fromItem(item);
    }

    public List<PlaceCollection> list(String userId, @Nullable String maxSortKey, int size) {
        Objects.requireNonNull(userId);

        QuerySpec query = new QuerySpec()
                .withHashKey("userId", userId)
                .withScanIndexForward(false)
                .withMaxResultSize(size);


        // Set min, max place sort
        if (maxSortKey != null) {
            query.withRangeKeyCondition(new RangeKeyCondition("sortKey").lt(maxSortKey));
        }

        ItemCollection<QueryOutcome> collection = sortIndex.query(query);

        // Collect results
        List<PlaceCollection> collections = new ArrayList<>();
        collection.forEach(item -> collections.add(fromItem(item)));
        return collections;
    }

    private PlaceCollection fromItem(Item item) {
        PlaceCollection placeCollection = new PlaceCollection();
        placeCollection.setUserId(item.getString("userId"));
        placeCollection.setCollectionId(item.getString("collectionId"));
        placeCollection.setSortKey(item.getString("sortKey"));

        placeCollection.setName(item.getString("name"));
        placeCollection.setDescription(item.getString("description"));

        placeCollection.setUpdatedDate(new Date(item.getLong("updatedDate")));
        placeCollection.setCreatedDate(new Date(item.getLong("createdDate")));
        return placeCollection;
    }
}
