package munch.api.place;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.ApiService;
import munch.api.place.basic.BasicCardLoader;
import munch.api.place.card.PlaceCard;
import munch.api.place.query.QueryCardLoader;
import munch.article.ArticleLinkClient;
import munch.article.data.Article;
import munch.data.client.PlaceClient;
import munch.data.place.Place;
import munch.gallery.PlaceImage;
import munch.gallery.PlaceImageClient;
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
public final class PlaceService extends ApiService {

    private final PlaceClient placeClient;
    private final BasicCardLoader basicReader;
    private final QueryCardLoader cardLoader;
    private final PlaceCardSorter cardSorter;

    private final ArticleLinkClient articleLinkClient;
    private final AwardCollectionClient awardCollectionClient;

    private final PlaceImageClient placeImageClient;

    @Inject
    public PlaceService(PlaceClient placeClient, BasicCardLoader basicReader, QueryCardLoader cardLoader,
                        PlaceCardSorter cardSorter, ArticleLinkClient articleLinkClient, AwardCollectionClient awardCollectionClient, PlaceImageClient placeImageClient) {
        this.placeClient = placeClient;
        this.basicReader = basicReader;
        this.cardLoader = cardLoader;
        this.cardSorter = cardSorter;
        this.articleLinkClient = articleLinkClient;
        this.awardCollectionClient = awardCollectionClient;
        this.placeImageClient = placeImageClient;
    }

    /**
     * Endpoint: /v/places/:placeId
     */
    @Override
    public void route() {
        PATH("/places/:placeId", () -> {
            GET("", this::get);
            GET("/cards", this::getCards);
            GET("/images", this::getImages);
            GET("/articles", this::getArticles);
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
                "awards", awardCollectionClient.list(placeId, null, 10),
                "articles", articleLinkClient.list(placeId, null, 10),
                "images", placeImageClient.list(placeId, null, 10)
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

    /**
     * @return all linked images
     */
    public NextNodeList<PlaceImage> getImages(JsonCall call) {
        String placeId = call.pathString("placeId");
        String nextSort = call.queryString("next.sort", null);
        int size = call.querySize(20, 40);

        return placeImageClient.list(placeId, nextSort, size);
    }

    private NextNodeList<Article> getArticles(JsonCall call) {
        int size = call.querySize(20, 40);
        String placeId = call.pathString("placeId");
        String nextSort = call.queryString("next.sort", null);

        return articleLinkClient.list(placeId, nextSort, size);
    }
}
