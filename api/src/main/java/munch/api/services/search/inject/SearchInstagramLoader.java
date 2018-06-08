package munch.api.services.search.inject;

import catalyst.utils.random.RandomUtils;
import munch.api.services.search.cards.SearchPartnerInstagramCard;
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
public final class SearchInstagramLoader implements SearchCardInjector.Loader {
    private int counter = 0;

    private final InstagramDiscoveryCardClient cardClient;
    private final PlaceClient placeClient;

    @Inject
    public SearchInstagramLoader(InstagramDiscoveryCardClient cardClient, PlaceClient placeClient) {
        this.cardClient = cardClient;
        this.placeClient = placeClient;
    }

    @Override
    public List<Position> load(Request request) {
        if (!request.isCardsMore(10)) return List.of();
        if (!request.isNatural()) return List.of();

        switch (RandomUtils.nextInt(0, 2)) {
            case 0:
                return List.of();
        }

        InstagramDiscoveryCard card = cardClient.get(counter);
        if (card == null) {
            // Reset
            counter = 0;
            return List.of();
        }

        counter++;

        SearchPartnerInstagramCard instagramCard = new SearchPartnerInstagramCard();
        instagramCard.setUserId(card.getUserId());
        instagramCard.setUsername(card.getUsername());
        instagramCard.setTitle(card.getTitle());

        instagramCard.setContents(
                placeClient.batchGetMap(card.getMediaList(), InstagramMedia::getPlaceId, (m, p) ->
                        new SearchPartnerInstagramCard.MediaContent(p, m))
        );

        return of(6, instagramCard);
    }
}
