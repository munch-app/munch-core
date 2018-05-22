package munch.api.services.search.cards;

import munch.collections.LikedPlaceClient;
import munch.data.structure.Place;
import munch.data.structure.SearchResult;
import munch.user.data.UserSetting;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/**
 * Created by: Fuxing
 * Date: 16/9/2017
 * Time: 5:38 PM
 * Project: munch-core
 */
@Singleton
public final class CardParser {

    private final LikedPlaceClient likedPlaceClient;

    @Inject
    public CardParser(LikedPlaceClient likedPlaceClient) {
        this.likedPlaceClient = likedPlaceClient;
    }

    /**
     * Only search results that is place is parsed now
     *
     * @param results search results
     * @return List of Parsed SearchCard
     */
    public List<SearchCard> parseCards(List<? extends SearchResult> results, @Nullable UserSetting setting) {
        List<SearchCard> cards = new ArrayList<>();
        for (SearchResult result : results) {
            // Since now there is only one card type, simple method to address the problem
            SearchCard card = parseCard(result);
            if (card != null) cards.add(card);
        }

        // Batch Resolve Liked Place
        if (setting != null) {
            Objects.requireNonNull(setting.getUserId());

            List<SearchPlaceCard> placeCards = new ArrayList<>();
            Set<String> placeIds = new HashSet<>();

            // Collect all PlaceCard & PlaceId
            for (SearchCard card : cards) {
                if (card instanceof SearchPlaceCard) {
                    placeCards.add((SearchPlaceCard) card);
                    placeIds.add(((SearchPlaceCard) card).getPlaceId());
                }
            }

            if (!placeIds.isEmpty()) {
                // Set all liked card
                Set<String> likedIds = likedPlaceClient.getIsLiked(setting.getUserId(), placeIds);
                for (SearchPlaceCard placeCard : placeCards) {
                    if (likedIds.contains(placeCard.getPlaceId())) {
                        placeCard.setLiked(true);
                    }
                }
            }
        }


        return cards;
    }

    /**
     * @param result parsable result
     * @return SearchCard
     */
    @Nullable
    public SearchCard parseCard(SearchResult result) {
        if (result instanceof Place) {
            Place place = (Place) result;
            if (place.getImages().isEmpty()) {
                return parseSmall(place);
            } else {
                return parse(place);
            }
        }
        return null;
    }

    private SearchCard parse(Place place) {
        SearchPlaceCard card = new SearchPlaceCard();
        card.setPlaceId(place.getId());
        card.setImages(place.getImages());
        card.setName(place.getName());

        card.setTags(place.getTag().getExplicits());
        card.setLocation(place.getLocation());
        card.setHours(place.getHours());
        card.setContainers(place.getContainers());
        card.setReview(place.getReview());
        return card;
    }

    private SearchCard parseSmall(Place place) {
        SearchSmallPlaceCard card = new SearchSmallPlaceCard();
        card.setPlaceId(place.getId());
        card.setImages(place.getImages());
        card.setName(place.getName());

        card.setTags(place.getTag().getExplicits());
        card.setLocation(place.getLocation());
        card.setHours(place.getHours());
        card.setContainers(place.getContainers());
        card.setReview(place.getReview());
        return card;
    }
}
