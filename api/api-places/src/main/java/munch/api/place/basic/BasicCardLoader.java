package munch.api.place.basic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edit.utils.website.DomainBlocked;
import munch.api.place.card.PlaceCard;
import munch.data.place.Place;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
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
public final class BasicCardLoader {
    private static final ObjectMapper objectMapper = JsonUtils.objectMapper;

    /**
     * @param place place to generate from
     * @return List of PlaceCard, non-null
     */
    public List<PlaceCard> generateCards(Place place) {
        List<AbstractPlaceCard> cards = new ArrayList<>();
        cards.add(createImageBanner(place));
        cards.add(createClosed(place));
        cards.add(createNameTag(place));
        cards.add(createAddress(place));
        cards.add(createDescription(place));
        cards.add(createWebsite(place));
        cards.add(createPhone(place));
        cards.add(createPrice(place));
        cards.add(createBusinessHour(place));
        cards.add(createLocation(place));
        cards.add(createMenu(place));
        cards.add(createSuggestEdit(place));

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
        card.setTags(place.getTags());
        return card;
    }

    private PlaceClosedCard createClosed(Place place) {
        if (place.getStatus() == null) return null;
        PlaceClosedCard card;
        switch (place.getStatus().getType()) {
            case open:
                return null;

            case moved:
                card = new PlaceClosedCard();
                card.setReason("Permanently Moved");
                return card;

            case renovation:
                card = new PlaceClosedCard();
                card.setReason("Renovation");
                return card;

            case closed:
            default:
                card = new PlaceClosedCard();
                card.setReason("Permanently Closed");
                return card;
        }
    }

    @Nullable
    private PlaceDescriptionCard createDescription(Place place) {
        String description = place.getDescription();
        if (StringUtils.isBlank(description)) return null;

        PlaceDescriptionCard card = new PlaceDescriptionCard();
        card.setDescription(description);
        return card;
    }

    @Nullable
    private PlaceWebsiteCard createWebsite(Place place) {
        String website = place.getWebsite();
        if (StringUtils.isBlank(website)) return null;

        PlaceWebsiteCard card = new PlaceWebsiteCard();
        card.setDomain(StringUtils.lowerCase(DomainBlocked.getTLD(website)));
        card.setWebsite(website);
        return card;
    }

    @Nullable
    private PlacePhoneCard createPhone(Place place) {
        String phone = place.getPhone();
        if (StringUtils.isEmpty(phone)) return null;

        PlacePhoneCard card = new PlacePhoneCard();
        card.setPhone(phone);
        return card;
    }

    @Nullable
    private PlacePriceCard createPrice(Place place) {
        if (place.getPrice() == null) return null;

        PlacePriceCard card = new PlacePriceCard();
        card.setPrice(place.getPrice().getPerPax());
        return card;
    }

    @Nullable
    private PlaceMenuCard createMenu(Place place) {
        if (place.getMenu() == null) return null;
        if (place.getMenu().getUrl() == null && (place.getMenu().getImages() == null || place.getMenu().getImages().isEmpty())) return null;

        PlaceMenuCard card = new PlaceMenuCard();
        card.setMenu(place.getMenu());
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
        card.setLocation(place.getLocation());
        return card;
    }

    private PlaceLocationCard createLocation(Place place) {
        PlaceLocationCard card = new PlaceLocationCard();
        card.setPlaceName(place.getName());
        card.setLocation(place.getLocation());
        return card;
    }

    private PlaceSuggestEditCard createSuggestEdit(Place place) {
        PlaceSuggestEditCard card = new PlaceSuggestEditCard();
        card.setPlaceId(place.getPlaceId());
        card.setName(place.getName());
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
