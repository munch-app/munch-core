package app.munch.migration;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 5:45 pm
 */
@Singleton
public final class EntityMigrationTable {
    private final Table table;

    @Inject
    EntityMigrationTable(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable("munch-core.EntityMigration");
    }

    public String getUID(String type, String id) {
        Item item = table.getItem("id", type + "." + id);
        if (item != null) {
            return item.getString("uid");
        }

        return null;
    }

    public void putUID(String type, String id, String uid) {
        Item item = new Item();
        item.withString("id", type + "." + id);
        item.withString("uid", uid);
        table.putItem(item);
    }
}
