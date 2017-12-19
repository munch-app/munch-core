package munch.api.services.places;

import munch.api.services.places.cards.PlaceHeaderCard;
import munch.data.structure.PlaceCard;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/10/2017
 * Time: 12:46 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceCardSorter {
    private static final CardGroup[] CARD_GROUPS = new CardGroup[]{
            // NOTE: CardId must be updated when it is updated on the card site
            CardGroup.of("basic_ImageBanner_20170915",
                    "basic_NameTag_20170912",
                    "basic_Address_20170924",
                    "basic_BusinessHour_20170907"),

            CardGroup.ofHeader("header_About_20171112",
                    "basic_Description_20171109",
                    "basic_Phone_20171117",
                    "basic_Website_20171109"),

            CardGroup.ofHeader("header_Instagram_20171208",
                    "vendor_InstagramMedia_20171204"),

            CardGroup.ofHeader("header_Article_20171112",
                    "vendor_Article_20171029"),

            CardGroup.ofHeader("header_Review_20171020",
                    "vendor_FacebookReview_20171017"),

            CardGroup.ofHeader("header_Location_20171112",
                    "basic_Location_20171112"),
    };

    /**
     * @param cards cards to sort in allowed order
     * @return sorted cards
     * @see PlaceCardSorter#CARD_GROUPS
     */
    public List<PlaceCard> sort(List<PlaceCard> cards) {
        List<PlaceCard> sorted = new ArrayList<>();
        for (CardGroup cardGroup : CARD_GROUPS) {
            sorted.addAll(cardGroup.collect(cards));
        }
        return sorted;
    }

    /**
     * CardGroup with header to group card together by their id
     */
    private static class CardGroup {
        private final PlaceCard headerCard;
        private final String[] contentCardIds;

        private CardGroup(@Nullable PlaceCard headerCard, String... contentCardIds) {
            this.headerCard = headerCard;
            this.contentCardIds = contentCardIds;
        }

        /**
         * @param cards card source to collect from
         * @return empty or contain cards
         */
        private List<PlaceCard> collect(List<PlaceCard> cards) {
            List<PlaceCard> contentCards = new ArrayList<>();
            for (String cardId : contentCardIds) {
                PlaceCard placeCard = find(cardId, cards);
                if (placeCard != null) contentCards.add(placeCard);
            }

            if (!isValid(contentCards)) return Collections.emptyList();

            if (headerCard != null) contentCards.add(0, headerCard);
            return contentCards;
        }

        /**
         * If extending this class, override for special implementation
         *
         * @param contentCards content cards
         * @return true is content cards are valid
         */
        protected boolean isValid(List<PlaceCard> contentCards) {
            return !contentCards.isEmpty();
        }

        @Nullable
        private PlaceCard find(String cardId, List<PlaceCard> cards) {
            for (PlaceCard card : cards) {
                if (card.getCardId().equals(cardId)) {
                    return card;
                }
            }
            return null;
        }

        private static CardGroup of(String... contentIds) {
            return new CardGroup(null, contentIds);
        }

        private static CardGroup ofHeader(PlaceCard headerCard, String... contentIds) {
            return new CardGroup(headerCard, contentIds);
        }

        private static CardGroup ofHeader(String headerId, String... contentIds) {
            return new CardGroup(new PlaceHeaderCard(headerId), contentIds);
        }
    }
}
