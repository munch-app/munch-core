package munch.api.services.search.cards;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.clients.StaticJsonResource;
import munch.data.Place;
import munch.data.SearchResult;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 16/9/2017
 * Time: 5:38 PM
 * Project: munch-core
 */
@Singleton
public final class CardParser {
    private final PlaceParser placeParser;

    @Inject
    private CardParser(PlaceParser placeParser) throws IOException {
        this.placeParser = placeParser;
    }

    /**
     * Only search results that is place is parsed now
     *
     * @param results search results
     * @return List of Parsed SearchCard
     */
    public List<SearchCard> parseCards(List<SearchResult> results) {
        List<SearchCard> cards = new ArrayList<>();
        for (SearchResult result : results) {
            // Since now there is only one card type, simple method to address the problem
            SearchCard card = parseCard(result);
            if (card != null) cards.add(card);
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
            return placeParser.parse((Place) result);
        }
        return null;
    }

    private final static class PlaceParser {
        private final Set<String> establishments = new HashSet<>();
        private final Set<String> tags = new HashSet<>();

        @Inject
        private PlaceParser(StaticJsonResource resource) throws IOException {
            JsonNode node = resource.getResource("tags-place-card.json");
            for (JsonNode establishment : node.path("establishments")) {
                establishments.add(establishment.asText().toLowerCase());
            }
            for (JsonNode cuisine : node.path("tags")) {
                tags.add(cuisine.asText().toLowerCase());
            }
        }

        private SearchPlaceCard parse(Place place) {
            SearchPlaceCard card = new SearchPlaceCard();
            card.setUniqueId(place.getId());
            card.setPlaceId(place.getId());
            card.setImages(place.getImages());
            card.setName(place.getName());

            card.setEstablishment(getEstablishment(place.getTags()));
            card.setTags(getTags(place.getTags()));
            card.setLocation(place.getLocation());
            card.setHours(place.getHours());
            return card;
        }

        private String getEstablishment(List<String> tags) {
            for (String tag : tags) {
                if (establishments.contains(tag.toLowerCase())) {
                    return WordUtils.capitalizeFully(tag);
                }
            }
            return "Restaurant";
        }

        private List<String> getTags(List<String> tags) {
            List<String> tagList = new ArrayList<>();
            for (String tag : tags) {
                if (this.tags.contains(tag.toLowerCase())) {
                    tagList.add(WordUtils.capitalizeFully(tag));
                }
            }
            return tagList;
        }
    }
}
