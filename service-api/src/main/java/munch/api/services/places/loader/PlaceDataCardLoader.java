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
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 6/3/18
 * Time: 8:30 PM
 * Project: munch-core
 */
public abstract class PlaceDataCardLoader<T extends ExtendedData, C extends ExtendedDataClient<T>> {
    protected static final ObjectMapper objectMapper = JsonUtils.objectMapper;

    protected final C client;
    protected final String cardId;
    protected final int size;

    public PlaceDataCardLoader(String cardId, C client, int size) {
        this.cardId = cardId;
        this.client = client;
        this.size = size;
    }

    protected List<T> query(String placeId) {
        return client.list(placeId, null, size);
    }

    public Optional<PlaceDataCard> load(Place place) {
        List<T> list = query(place.getId());

        if (list.isEmpty()) return Optional.empty();
        return Optional.of(new PlaceDataCard(list));
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
