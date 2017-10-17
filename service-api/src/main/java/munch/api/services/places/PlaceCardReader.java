package munch.api.services.places;

import munch.data.clients.PlaceCardClient;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/8/2017
 * Time: 6:11 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceCardReader {
    private final PlaceClient placeClient;
    private final PlaceCardClient cardClient;

    private final BasicCardReader basicReader;
    private final PlaceCardSorter cardSorter;

    @Inject
    public PlaceCardReader(PlaceClient placeClient, PlaceCardClient cardClient, BasicCardReader basicReader, PlaceCardSorter cardSorter) {
        this.placeClient = placeClient;
        this.cardClient = cardClient;
        this.basicReader = basicReader;
        this.cardSorter = cardSorter;
    }

    /**
     * Return type is List because data return order is important
     *
     * @param placeId placeId
     * @return List of AbstractCard, or null if place cannot be found
     */
    @Nullable
    public List<PlaceCard> get(String placeId) {
        Place place = placeClient.get(placeId);
        if (place == null) return null;

        List<PlaceCard> cards = new ArrayList<>();
        cards.addAll(basicReader.generateCards(place));
        cards.addAll(cardClient.list(placeId));
        return cardSorter.sort(cards);
    }
}
