package munch.collections;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import munch.restful.core.exception.ParamException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by: Fuxing
 * Date: 9/1/18
 * Time: 3:38 PM
 * Project: munch-core
 */
@Singleton
public final class CollectionClient {
    private static final Pattern PATTERN_UUID = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
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

        if (StringUtils.isBlank(collection.getCollectionId())) {
            collection.setCollectionId(UUID.randomUUID().toString());
        }
        if (StringUtils.isBlank(collection.getSortKey())) {
            collection.setSortKey(String.valueOf(System.currentTimeMillis()));
        }
        if (collection.getCreatedDate() == null) {
            collection.setCreatedDate(new Date());
        }

        collection.setUpdatedDate(new Date());

        // TODO Many Collection
        validateUUID(collection.getCollectionId(), "collectionId");

        validateLength(collection.getName(), 100, "name");
        validateLength(collection.getDescription(), 500, "description");

        Item item = new Item();
        item.with("userId", collection.getUserId());
        item.with("collectionId", collection.getCollectionId());
        item.with("sortKey", collection.getSortKey());

        item.with("name", collection.getName());
        item.with("description", collection.getDescription());

        item.with("updatedDate", collection.getUpdatedDate().getTime());
        item.with("createdDate", collection.getCreatedDate().getTime());
        table.putItem(item);
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

    public static void validateLength(String text, int length, String type) {
        Objects.requireNonNull(text);
        if (text.length() < length) return;
        throw new ParamException("Failed Text Length Validation for " + type);
    }

    public static void validateUUID(String id, String type) {
        Objects.requireNonNull(id, "Id Requires non null");
        if (PATTERN_UUID.matcher(id).matches()) return;
        throw new ParamException("Failed Id Validation for " + type);
    }
}
