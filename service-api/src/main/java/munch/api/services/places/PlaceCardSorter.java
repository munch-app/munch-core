package munch.api.services.places;

import munch.data.structure.PlaceCard;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/10/2017
 * Time: 12:46 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceCardSorter {
    private static final String[] CARD_ORDER = new String[]{
            "basic_ImageBanner_20170915",
            "basic_NameTag_20170912",
            "basic_Address_20170924",
            "basic_BusinessHour_20170907",
            "vendor_FacebookReview_20171017",
            "basic_Location_20170924",
    };

    /**
     * @param cards cards to sort in allowed order
     * @return sorted cards
     * @see PlaceCardSorter#CARD_ORDER
     */
    public List<PlaceCard> sort(List<PlaceCard> cards) {
        List<PlaceCard> sorted = new ArrayList<>();
        for (String cardId : CARD_ORDER) {
            PlaceCard placeCard = find(cardId, cards);
            if (placeCard != null) sorted.add(placeCard);
        }
        return sorted;
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
}
