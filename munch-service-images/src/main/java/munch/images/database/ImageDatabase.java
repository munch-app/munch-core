package munch.images.database;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 19/4/2017
 * Time: 12:52 AM
 * Project: munch-core
 */
@Singleton
public class ImageDatabase {
    private static final String TableName = "munch-service-images.Image";

    private final DynamoDB dynamoDB;
    private final Table table;

    @Inject
    public ImageDatabase(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
        this.table = dynamoDB.getTable(TableName);
    }

    public Image get(String key) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("k", key);
        Item item = table.getItem(spec);
        // Don't exist
        if (item == null) return null;

        Image image = new Image();
        image.setKey(item.getString("k"));
        image.setKinds(mapToSet(item.getList("t")));
        return image;
    }

    public List<Image> get(List<String> keys) {
        TableKeysAndAttributes attributes = new TableKeysAndAttributes(TableName);
        keys.forEach(key -> attributes.addHashOnlyPrimaryKey("k", key));
        BatchGetItemOutcome outcome = dynamoDB.batchGetItem(attributes);
        return outcome.getTableItems().get(TableName).stream()
                .map(item -> {
                    Image image = new Image();
                    image.setKey(item.getString("k"));
                    image.setKinds(mapToSet(item.getList("t")));
                    return image;
                }).collect(Collectors.toList());
    }

    public void put(Image image) {
        table.putItem(new Item()
                .withPrimaryKey("k", image.getKey())
                .withList("t", setToMaps(image.getKinds())));
    }

    public void delete(String key) {
        table.deleteItem("k", key);
    }

    /**
     * Only these are persisted
     * <pre>
     *     {
     *         "t": "thumbnail",
     *         "k": "key"
     *     }
     * </pre>
     *
     * @param kinds list of Type
     * @return List of Type in Map structure
     */
    private static List<Map<String, String>> setToMaps(Set<Image.Kind> kinds) {
        return kinds.stream()
                .map(kind -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("t", ImageKind.toValue(kind.getKind()));
                    map.put("k", kind.getKey());
                    return map;
                }).collect(Collectors.toList());
    }

    /**
     * t = type
     * k = key
     *
     * @param maps list of map
     * @return List of Type in bean
     * @see ImageDatabase#setToMaps(Set) (List)
     */
    private static Set<Image.Kind> mapToSet(List<Map<String, String>> maps) {
        return maps.stream()
                .map(map -> {
                    Image.Kind kind = new Image.Kind();
                    kind.setKind(ImageKind.forValue(map.get("t")));
                    kind.setKey(map.get("k"));
                    return kind;
                }).collect(Collectors.toSet());
    }
}
