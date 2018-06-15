package munch.api.place;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.ApiService;
import munch.api.place.basic.BasicCardLoader;
import munch.api.place.card.PlaceCard;
import munch.api.place.query.QueryCardLoader;
import munch.data.client.PlaceClient;
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

    @Inject
    public PlaceService(PlaceClient placeClient, BasicCardLoader basicReader, QueryCardLoader cardLoader, PlaceCardSorter cardSorter) {
        this.placeClient = placeClient;
        this.basicReader = basicReader;
        this.cardLoader = cardLoader;
        this.cardSorter = cardSorter;
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
    private Place get(JsonCall call) {
        return placeClient.get(call.pathString("placeId"));
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
