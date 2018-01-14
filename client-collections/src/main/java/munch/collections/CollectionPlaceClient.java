package munch.collections;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.Select;

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
 * Time: 4:02 PM
 * Project: munch-core
 */
@Singleton
public final class CollectionPlaceClient {
    private static final String DYNAMO_TABLE_NAME = "munch-core.AddedPlace";

    private final Table table;
    private final Index sortIndex;

    @Inject
    public CollectionPlaceClient(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable(DYNAMO_TABLE_NAME);
        this.sortIndex = table.getIndex("sortKey-index");
    }

    public boolean isAdded(String userId, String collectionId, String placeId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(collectionId);
        Objects.requireNonNull(placeId);

        GetItemSpec getSpec = new GetItemSpec();
        getSpec.withPrimaryKey("uc", createKey(userId, collectionId), "p", placeId);
        getSpec.withAttributesToGet("uc", "p");
        return table.getItem(getSpec) != null;
    }

    public void add(String userId, String collectionId, String placeId) {
        Objects.requireNonNull(userId);
        CollectionClient.validateUUID(collectionId, "collectionId");
        CollectionClient.validateUUID(placeId, "placeId");

        Item item = new Item();
        item.with("uc", createKey(userId, collectionId));
        item.with("p", placeId);
        item.with("s", String.valueOf(System.currentTimeMillis()));
        item.with("c", System.currentTimeMillis());
        table.putItem(item);
    }

    public void remove(String userId, String collectionId, String placeId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(collectionId);
        Objects.requireNonNull(placeId);
        table.deleteItem("uc", createKey(userId, collectionId), "p", placeId);
    }

    public long count(String userId, String collectionId) {
        // TODO count need more efficient method
        Objects.requireNonNull(userId);
        Objects.requireNonNull(collectionId);

        QuerySpec query = new QuerySpec()
                .withHashKey("uc", createKey(userId, collectionId))
                .withSelect(Select.COUNT);
        return table.query(query).getAccumulatedItemCount();
    }

    public List<AddedPlace> list(String userId, String collectionId, @Nullable String maxSortKey, int size) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(collectionId);

        QuerySpec query = new QuerySpec()
                .withHashKey("uc", createKey(userId, collectionId))
                .withScanIndexForward(false)
                .withMaxResultSize(size);


        // Set min, max place sort
        if (maxSortKey != null) {
            query.withRangeKeyCondition(new RangeKeyCondition("s").lt(maxSortKey));
        }

        ItemCollection<QueryOutcome> collection = sortIndex.query(query);

        // Collect results
        List<AddedPlace> addedPlaces = new ArrayList<>();
        collection.forEach(item -> addedPlaces.add(fromItem(item)));
        return addedPlaces;
    }

    private static String createKey(String userId, String collectionId) {
        return userId + "_" + collectionId;
    }

    private static AddedPlace fromItem(Item item) {
        AddedPlace addedPlace = new AddedPlace();
        addedPlace.setPlaceId(item.getString("p"));
        addedPlace.setSortKey(item.getString("s"));
        addedPlace.setCreatedDate(new Date(item.getLong("c")));
        return addedPlace;
    }
}
