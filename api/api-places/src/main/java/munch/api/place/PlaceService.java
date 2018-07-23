package munch.api.place;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.ApiService;
import munch.api.place.basic.BasicCardLoader;
import munch.api.place.card.PlaceCard;
import munch.api.place.query.QueryCardLoader;
import munch.article.clients.ArticleClient;
import munch.corpus.instagram.InstagramMediaClient;
import munch.data.client.PlaceClient;
import munch.data.extended.PlaceAwardClient;
import munch.data.extended.PlaceMenuClient;
import munch.data.place.Place;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonResult;

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

    private final ArticleClient articleClient;
    private final InstagramMediaClient instagramMediaClient;
    private final PlaceMenuClient placeMenuClient;
    private final PlaceAwardClient placeAwardClient;

    @Inject
    public PlaceService(PlaceClient placeClient, BasicCardLoader basicReader, QueryCardLoader cardLoader, PlaceCardSorter cardSorter, ArticleClient articleClient, InstagramMediaClient instagramMediaClient, PlaceMenuClient placeMenuClient, PlaceAwardClient placeAwardClient) {
        this.placeClient = placeClient;
        this.basicReader = basicReader;
        this.cardLoader = cardLoader;
        this.cardSorter = cardSorter;

        this.articleClient = articleClient;
        this.instagramMediaClient = instagramMediaClient;
        this.placeMenuClient = placeMenuClient;
        this.placeAwardClient = placeAwardClient;
    }

    /**
     * Endpoint: /v/places/:placeId
     */
    @Override
    public void route() {
        // Places Endpoint
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
        return JsonResult.ok(Map.of(
                "place", place,
                "articles", articleClient.list(placeId, null, 10),
                "instagram", Map.of(
                        "medias", instagramMediaClient.listByPlace(placeId, null, 10)
                ),
                "menus", placeMenuClient.list(placeId, null, 10),
                "awards", placeAwardClient.list(placeId, null, 10)
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
