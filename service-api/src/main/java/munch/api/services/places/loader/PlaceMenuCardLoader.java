package munch.api.services.places.loader;

import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.data.extended.PlaceMenu;
import munch.data.extended.PlaceMenuClient;
import munch.data.structure.Place;
import munch.restful.core.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 6/3/18
 * Time: 8:37 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceMenuCardLoader extends PlaceDataCardLoader<PlaceMenu, PlaceMenuClient> {

    @Inject
    public PlaceMenuCardLoader(PlaceMenuClient client) {
        super("extended_PlaceMenu_20180306", client, 15);
    }

    @Override
    public Optional<PlaceDataCard> load(Place place) {
        List<PlaceMenu> dataList = query(place.getId());

        String menuUrl = place.getMenuUrl();
        if (menuUrl == null && dataList.isEmpty()) return Optional.empty();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("menuUrl", menuUrl);
        objectNode.set("images", JsonUtils.toTree(dataList));

        return Optional.of(new PlaceDataCard(objectNode));
    }
}
