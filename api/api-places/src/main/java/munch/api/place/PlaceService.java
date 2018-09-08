package munch.api.place;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.ApiService;
import munch.api.place.basic.BasicCardLoader;
import munch.api.place.card.PlaceCard;
import munch.api.place.query.PlaceArticleCardLoader;
import munch.api.place.query.QueryCardLoader;
import munch.article.clients.Article;
import munch.data.client.PlaceClient;
import munch.data.place.Place;
import munch.instagram.InstagramLinkClient;
import munch.restful.core.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;
import munch.user.client.AwardCollectionClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceService extends ApiService {

    private final PlaceClient placeClient;
    private final BasicCardLoader basicReader;
    private final QueryCardLoader cardLoader;
    private final PlaceCardSorter cardSorter;

    private final InstagramLinkClient instagramLinkClient;
    private final AwardCollectionClient awardCollectionClient;

    private final CatalystV2Support v2Support;

    @Inject
    public PlaceService(PlaceClient placeClient, BasicCardLoader basicReader, QueryCardLoader cardLoader, PlaceCardSorter cardSorter, InstagramLinkClient instagramLinkClient, AwardCollectionClient awardCollectionClient, CatalystV2Support v2Support) {
        this.placeClient = placeClient;
        this.basicReader = basicReader;
        this.cardLoader = cardLoader;
        this.cardSorter = cardSorter;
        this.instagramLinkClient = instagramLinkClient;
        this.awardCollectionClient = awardCollectionClient;

        this.v2Support = v2Support;
    }

    /**
     * Endpoint: /v/places/:placeId
     */
    @Override
    public void route() {
        PATH("/places/:placeId", () -> {
            GET("", this::get);
            GET("/cards", this::getCards);
        });
    }

    /**
     * GET = /places/:placeId
     *
     * @param call json call
     * @return Place or Null
     */
    private JsonResult get(JsonCall call) {
        String placeId = call.pathString("placeId");
        Place place = placeClient.get(placeId);
        if (place == null) return JsonResult.notFound();

        NextNodeList<Article> articles = v2Support.getArticles(placeId, null, 10);
        PlaceArticleCardLoader.removeBadData(articles);

        return JsonResult.ok(Map.of(
                "place", place,
                "awards", awardCollectionClient.list(placeId, null, 10),
                "articles", articles,
                "instagram", Map.of(
                        "medias", instagramLinkClient.list(placeId, null, 10)
                )
        ));
    }

    /**
     * GET = /places/:placeId/cards
     *
     * @param call json call
     * @return {cards: List of PlaceCard, place: Place}
     */
    private JsonResult getCards(JsonCall call) {
        Place place = placeClient.get(call.pathString("placeId"));
        if (place == null) return JsonResult.notFound();

        List<PlaceCard> cards = new ArrayList<>();
        cards.addAll(basicReader.generateCards(place));
        cards.addAll(cardLoader.load(place));
        cards = cardSorter.sort(cards);

        return JsonResult.ok(Map.of(
                "cards", cards,
                "place", place
        ));
    }
}
