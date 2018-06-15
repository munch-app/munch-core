package munch.api.search.cards;

import munch.data.ElasticObject;
import munch.data.place.Place;
import munch.user.data.UserSetting;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 16/9/2017
 * Time: 5:38 PM
 * Project: munch-core
 */
@Singleton
public final class CardParser {

    /**
     * Only search results that is place is parsed now
     *
     * @param results search results
     * @return List of Parsed SearchCard
     */
    public List<SearchCard> parseCards(List<? extends ElasticObject> results, @Nullable UserSetting setting) {
        List<SearchCard> cards = new ArrayList<>();
        for (ElasticObject result : results) {
            cards.addAll(parseCard(result));
        }

        return cards;
    }

    /**
     * @param object Elastic object to parse to card
     * @return List of Parsed SearchCard
     */
    public List<SearchCard> parseCard(ElasticObject object) {
        if (object instanceof Place) {
            return List.of(parse((Place) object));
        }
        return null;
    }

    private SearchPlaceCard parse(Place place) {
        if (place.getImages().isEmpty()) {
            SearchPlaceCard.Small small = new SearchPlaceCard.Small();
            small.setPlace(place);
            return small;
        } else {
            SearchPlaceCard card = new SearchPlaceCard();
            card.setPlace(place);
            return card;
        }
    }
}
