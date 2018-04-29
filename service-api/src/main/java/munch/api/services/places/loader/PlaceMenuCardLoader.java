package munch.api.services.places.loader;

import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.data.extended.PlaceMenu;
import munch.data.extended.PlaceMenuClient;
import munch.data.structure.Place;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/3/18
 * Time: 8:37 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceMenuCardLoader extends PlaceDataCardLoader.Extended<PlaceMenu, PlaceMenuClient> {

    @Inject
    public PlaceMenuCardLoader(PlaceMenuClient client) {
        super("extended_PlaceMenu_20180313", client, 15);
    }

    @Override
    public List<PlaceDataCard> load(Place place) {
        List<PlaceMenu> dataList = query(place.getId());
        if (dataList.isEmpty()) return List.of();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("images", JsonUtils.toTree(dataList));

        return List.of(new PlaceDataCard(objectNode));
    }
}
