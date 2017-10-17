package munch.api.services.places;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import munch.api.services.places.cards.*;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:54 AM
 * Project: munch-core
 */
@Singleton
public final class BasicCardReader {
    private final ObjectMapper objectMapper;

    @Inject
    public BasicCardReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @param place place to generate from
     * @return List of PlaceCard, non-null
     */
    public List<PlaceCard> generateCards(Place place) {
        List<AbstractPlaceCard> cards = new ArrayList<>();
        cards.add(createImageBanner(place));
        cards.add(createNameTag(place));
        cards.add(createAddress(place));
        cards.add(createBusinessHour(place));
        cards.add(createLocation(place));

        return cards.stream().filter(Objects::nonNull)
                .map(card -> new BasicPlaceCard(card.getCardId(), objectMapper.valueToTree(card)))
                .collect(Collectors.toList());
    }

    private PlaceImageBannerCard createImageBanner(Place place) {
        PlaceImageBannerCard card = new PlaceImageBannerCard();
        card.setImages(place.getImages());
        return card;
    }

    private PlaceNameTagCard createNameTag(Place place) {
        PlaceNameTagCard card = new PlaceNameTagCard();
        card.setName(place.getName());
        card.setTags(ImmutableSet.copyOf(place.getTag().getExplicits()));
        return card;
    }

    @Nullable
    private PlaceBusinessHourCard createBusinessHour(Place place) {
        if (place.getHours() == null) return null;
        if (place.getHours().isEmpty()) return null;

        PlaceBusinessHourCard card = new PlaceBusinessHourCard();
        card.setHours(place.getHours());
        return card;
    }

    private PlaceAddressCard createAddress(Place place) {
        PlaceAddressCard card = new PlaceAddressCard();
        card.setAddress(place.getLocation().getAddress());

        card.setStreet(place.getLocation().getStreet());
        card.setUnitNumber(place.getLocation().getUnitNumber());
        card.setCity(place.getLocation().getCity());
        card.setCountry(place.getLocation().getCountry());
        card.setPostal(place.getLocation().getPostal());

        card.setLatLng(place.getLocation().getLatLng());
        card.setNearestTrain(place.getLocation().getNearestTrain());
        return card;
    }

    private PlaceLocationCard createLocation(Place place) {
        PlaceLocationCard card = new PlaceLocationCard();
        card.setPlaceName(place.getName());
        card.setLatLng(place.getLocation().getLatLng());
        card.setAddress(place.getLocation().getAddress());
        return card;
    }

    /**
     * BasicPlaceCard Transformer
     */
    private static class BasicPlaceCard implements PlaceCard<JsonNode> {
        private final String cardId;
        private final JsonNode data;

        private BasicPlaceCard(String cardId, JsonNode data) {
            this.cardId = cardId;
            this.data = data;
        }

        @Override
        public String getCardId() {
            return cardId;
        }

        @Override
        public JsonNode getData() {
            return data;
        }
    }
}
