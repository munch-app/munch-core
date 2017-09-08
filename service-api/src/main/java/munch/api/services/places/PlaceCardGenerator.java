package munch.api.services.places;

import munch.api.clients.DataClient;
import munch.data.Place;
import munch.data.places.PlaceCard;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 18/8/2017
 * Time: 6:11 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceCardGenerator {

    private final DataClient dataClient;
    private final BasicCardGenerator basicGenerator;

    @Inject
    public PlaceCardGenerator(DataClient dataClient, BasicCardGenerator basicGenerator) {
        this.dataClient = dataClient;
        this.basicGenerator = basicGenerator;
    }

    /**
     * Return type is List because data return order is important
     *
     * @param placeId placeId
     * @return List of AbstractCard, or null if place cannot be found
     */
    @Nullable
    public List<PlaceCard> generate(String placeId) {
        Place place = dataClient.get(placeId);
        if (place == null) return null;

        List<PlaceCard> cards = new ArrayList<>();
        // Generate Basic
        cards.addAll(basicGenerator.generate(place));
        // Generate Vendor

        // Remove all that cards that is null
        cards.removeIf(Objects::isNull);
        return cards;
    }
}
