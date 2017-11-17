package munch.api.services.places;

import munch.data.clients.PlaceCardClient;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;

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
public final class PlaceCardReader {
    private final PlaceCardClient cardClient;

    private final BasicCardReader basicReader;
    private final PlaceCardSorter cardSorter;

    @Inject
    public PlaceCardReader(PlaceCardClient cardClient, BasicCardReader basicReader, PlaceCardSorter cardSorter) {
        this.cardClient = cardClient;
        this.basicReader = basicReader;
        this.cardSorter = cardSorter;
    }

    /**
     * Return type is List because data return order is important
     *
     * @param place with placeId
     * @return List of AbstractCard, or null if place cannot be found
     */
    @Nullable
    public List<PlaceCard> get(Place place) {
        Objects.requireNonNull(place.getId());

        List<PlaceCard> cards = new ArrayList<>();
        cards.addAll(basicReader.generateCards(place));
        cards.addAll(cardClient.list(place.getId()));
        return cardSorter.sort(cards);
    }
}
