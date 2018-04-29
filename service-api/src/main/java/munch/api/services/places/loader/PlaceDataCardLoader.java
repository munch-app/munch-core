package munch.api.services.places.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.data.extended.ExtendedData;
import munch.data.extended.ExtendedDataClient;
import munch.data.structure.Place;
import munch.data.structure.PlaceJsonCard;
import munch.restful.core.JsonUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 27/4/2018
 * Time: 7:36 PM
 * Project: munch-core
 */
public abstract class PlaceDataCardLoader<T> {
    protected static final ObjectMapper objectMapper = JsonUtils.objectMapper;

    protected final String cardId;

    public PlaceDataCardLoader(String cardId) {
        this.cardId = cardId;
    }

    protected abstract List<T> query(String placeId);

    public List<? extends PlaceJsonCard> load(Place place) {
        List<T> list = query(place.getId());

        if (list.isEmpty()) return List.of();
        return List.of(new PlaceDataCard(list));
    }

    public static class Extended<T extends ExtendedData, C extends ExtendedDataClient<T>> extends PlaceDataCardLoader<T> {
        protected final C client;
        protected final int size;

        public Extended(String cardId, C client, int size) {
            super(cardId);
            this.client = client;
            this.size = size;
        }

        protected List<T> query(String placeId) {
            return client.list(placeId, null, size);
        }

    }

    public class PlaceDataCard extends PlaceJsonCard {
        public PlaceDataCard(List dataList) {
            super(cardId, dataList);
        }

        public PlaceDataCard(Map<String, Object> map) {
            super(cardId, JsonUtils.toTree(map));
        }

        public PlaceDataCard(ObjectNode objectNode) {
            super(cardId, objectNode);
        }
    }
}
