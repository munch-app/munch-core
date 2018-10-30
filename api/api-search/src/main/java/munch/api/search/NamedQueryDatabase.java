package munch.api.search;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import munch.api.search.data.NamedSearchQuery;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ValidationException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 31/10/18
 * Time: 2:36 AM
 * Project: munch-core
 */
@Singleton
public final class NamedQueryDatabase {

    private final Table table;

    @Inject
    public NamedQueryDatabase(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable("munch-core.NamedSearchQuery");
    }

    public NamedSearchQuery get(String name) {
        Item item = table.getItem("name", name);
        if (item == null) return null;

        return JsonUtils.toObject(item.toJSON(), NamedSearchQuery.class);
    }

    public void put(NamedSearchQuery query) {
        query.setUpdatedMillis(System.currentTimeMillis());
        ValidationException.validate(query);

        Item item = Item.fromJSON(JsonUtils.toString(query));
        table.putItem(item);
    }
}
