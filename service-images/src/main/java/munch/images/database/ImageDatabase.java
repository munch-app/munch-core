package munch.images.database;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 19/4/2017
 * Time: 12:52 AM
 * Project: munch-core
 */
@Singleton
public class ImageDatabase {
    public static final String TableName = "munch-images.Image";

    private final DynamoDB dynamoDB;
    private final Table table;

    @Inject
    public ImageDatabase(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
        this.table = dynamoDB.getTable(TableName);
    }

    public Image get(String key) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(Scheme.Key, key);
        Item item = table.getItem(spec);
        // Don't exist
        if (item == null) return null;

        Image image = new Image();
        image.setKey(item.getString(Scheme.Key));
        image.setContentType(item.getString(Scheme.ContentType));
        image.setCreated(new Date(item.getLong(Scheme.Created)));
        image.setKinds(mapToSet(item.getList(Scheme.Kinds)));
        return image;
    }

    public List<Image> get(List<String> keys) {
        TableKeysAndAttributes attributes = new TableKeysAndAttributes(TableName);
        keys.forEach(key -> attributes.addHashOnlyPrimaryKey(Scheme.Key, key));
        BatchGetItemOutcome outcome = dynamoDB.batchGetItem(attributes);
        return outcome.getTableItems().get(TableName).stream()
                .map(item -> {
                    Image image = new Image();
                    image.setKey(item.getString(Scheme.Key));
                    image.setContentType(item.getString(Scheme.ContentType));
                    image.setCreated(new Date(item.getLong(Scheme.Created)));
                    image.setKinds(mapToSet(item.getList(Scheme.Kinds)));
                    return image;
                }).collect(Collectors.toList());
    }

    public void put(Image image) {
        table.putItem(new Item()
                .withPrimaryKey(Scheme.Key, image.getKey())
                .withString(Scheme.ContentType, image.getContentType())
                .withLong(Scheme.Created, image.getCreated().getTime())
                .withList(Scheme.Kinds, setToMaps(image.getKinds())));
    }

    public void delete(String key) {
        table.deleteItem(Scheme.Key, key);
    }

    /**
     * Only these are persisted
     * <pre>
     *     {
     *         "t": "thumbnail",
     *         "k": Scheme.Key
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
                    map.put(Scheme.Kind.Kind, kind.getKind().getName());
                    map.put(Scheme.Kind.Key, kind.getKey());
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
                    kind.setKind(ImageKind.forValue(map.get(Scheme.Kind.Kind)));
                    kind.setKey(map.get(Scheme.Kind.Key));
                    return kind;
                }).collect(Collectors.toSet());
    }

    /**
     * Scheme for image to be stored in dynamo db
     */
    private static final class Scheme {
        private static final String Key = "key";

        private static final String ContentType = "contentType";
        private static final String Created = "created";
        private static final String Kinds = "kinds";

        private static final class Kind {
            private static final String Key = "key";
            private static final String Kind = "kind";
        }
    }
}
