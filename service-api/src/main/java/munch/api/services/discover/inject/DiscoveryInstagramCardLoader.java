package munch.api.services.discover.inject;

import catalyst.utils.random.RandomUtils;
import munch.api.services.discover.cards.SearchCard;
import munch.api.services.discover.cards.SearchPartnerInstagramCard;
import munch.corpus.instagram.InstagramDiscoveryCard;
import munch.corpus.instagram.InstagramDiscoveryCardClient;
import munch.corpus.instagram.InstagramMedia;
import munch.data.clients.PlaceClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 5/5/18
 * Time: 4:54 PM
 * Project: munch-core
 */
@Singleton
public final class DiscoveryInstagramCardLoader {
    private int counter = 0;

    private final InstagramDiscoveryCardClient cardClient;
    private final PlaceClient placeClient;

    @Inject
    public DiscoveryInstagramCardLoader(InstagramDiscoveryCardClient cardClient, PlaceClient placeClient) {
        this.cardClient = cardClient;
        this.placeClient = placeClient;
    }

    public List<SearchCard> load() {
        switch (RandomUtils.nextInt(0, 2)) {
            case 1:
            default:
                return get();
                // TODO Before Release
//                return List.of();
        }
    }

    private List<SearchCard> get() {
        InstagramDiscoveryCard card = cardClient.get(counter);

        if (card == null) {
            // Reset
            counter = 0;
            return null;
        }

        counter++;

        SearchPartnerInstagramCard instagramCard = new SearchPartnerInstagramCard();
        instagramCard.setUserId(card.getUserId());
        instagramCard.setUsername(card.getUsername());
        instagramCard.setTitle(card.getTitle());

        List<SearchPartnerInstagramCard.MediaContent> contents = placeClient.batchGetMap(card.getMediaList(), InstagramMedia::getPlaceId, (instagramMedia, place) -> {
            SearchPartnerInstagramCard.MediaContent content = new SearchPartnerInstagramCard.MediaContent();
            content.setPlace(place);
            content.setCaption(instagramMedia.getCaption());
            content.setImages(instagramMedia.getImages());
            content.setMediaId(instagramMedia.getMediaId());
            content.setLocationId(instagramMedia.getLocationId());
            return content;
        });
        instagramCard.setContents(contents);
        return List.of(instagramCard);
    }
}
