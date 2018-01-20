package munch.collections;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import munch.restful.core.exception.ParamException;
import munch.restful.core.exception.ValidationException;
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

    /**
     * Leaky Bucket or similar solution should be added on other endpoint to either prevent too many collections or abuse of system
     *
     * @param collection collection to add
     */
    public void put(PlaceCollection collection) {
        Objects.requireNonNull(collection.getUserId());
        Objects.requireNonNull(collection.getName());

        // Optional Inject
        if (StringUtils.isBlank(collection.getCollectionId())) collection.setCollectionId(UUID.randomUUID().toString());
        if (collection.getSortKey() == 0) collection.setSortKey(System.currentTimeMillis());
        if (collection.getCreatedDate() == null) collection.setCreatedDate(new Date());

        // Mandatory Inject
        collection.setUpdatedDate(new Date());

        // Validate PlaceCollection based on bean validator
        ValidationException.validate(collection);

        table.putItem(toItem(collection));
    }

    public void delete(String userId, String collectionId) {
        Objects.requireNonNull(userId);
        validateUUID(collectionId, "collectionId");
        table.deleteItem("u", userId, "c", collectionId);
    }

    public PlaceCollection get(String userId, String collectionId) {
        Objects.requireNonNull(userId);
        validateUUID(collectionId, "collectionId");

        Item item = table.getItem("u", userId, "c", collectionId);
        if (item == null) return null;
        return fromItem(item);
    }

    public List<PlaceCollection> list(String userId, @Nullable Long maxSortKey, int size) {
        Objects.requireNonNull(userId);

        QuerySpec query = new QuerySpec()
                .withHashKey("u", userId)
                .withScanIndexForward(false)
                .withMaxResultSize(size);


        // Set min, max place sort
        if (maxSortKey != null) {
            query.withRangeKeyCondition(new RangeKeyCondition("s").lt(maxSortKey));
        }

        ItemCollection<QueryOutcome> collection = sortIndex.query(query);

        // Collect results
        List<PlaceCollection> collections = new ArrayList<>();
        collection.forEach(item -> collections.add(fromItem(item)));
        return collections;
    }

    private Item toItem(PlaceCollection collection) {
        Item item = new Item();
        item.with("u", collection.getUserId());
        item.with("c", collection.getCollectionId());
        item.with("s", collection.getSortKey());

        item.with("n", collection.getName());
        item.with("d", collection.getDescription());
        item.with("pc", collection.getCount());

        item.with("t", collection.getThumbnail());

        item.with("ud", collection.getUpdatedDate().getTime());
        item.with("cd", collection.getCreatedDate().getTime());
        return item;
    }

    private PlaceCollection fromItem(Item item) {
        PlaceCollection collection = new PlaceCollection();
        collection.setUserId(item.getString("u"));
        collection.setCollectionId(item.getString("c"));
        collection.setSortKey(item.getLong("s"));

        collection.setName(item.getString("n"));
        collection.setDescription(item.getString("d"));
        collection.setCount(item.getLong("pc"));

        collection.setThumbnail(item.getMap("t"));

        collection.setUpdatedDate(new Date(item.getLong("ud")));
        collection.setCreatedDate(new Date(item.getLong("cd")));
        return collection;
    }

    public static void validateUUID(String id, String type) {
        Objects.requireNonNull(id, "Id Requires non null");
        if (PATTERN_UUID.matcher(id).matches()) return;
        throw new ParamException("Failed Id Validation for " + type);
    }
}
