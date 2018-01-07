package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.places.PlaceCardReader;
import munch.article.clients.Article;
import munch.article.clients.ArticleClient;
import munch.corpus.instagram.InstagramMedia;
import munch.corpus.instagram.InstagramMediaClient;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2/4/2017
 * Time: 4:15 PM
 * Project: munch-core
 */
@Singleton
public class PlaceService extends AbstractService {
    private final PlaceClient dataClient;
    private final ArticleClient articleClient;
    private final InstagramMediaClient instagramMediaClient;
    private final PlaceCardReader cardReader;

    @Inject
    public PlaceService(PlaceClient placeClient, ArticleClient articleClient, InstagramMediaClient instagramMediaClient, PlaceCardReader cardReader) {
        this.dataClient = placeClient;
        this.articleClient = articleClient;
        this.instagramMediaClient = instagramMediaClient;
        this.cardReader = cardReader;
    }

    /**
     * Endpoint: /v/places/*
     * Endpoint: /v/places/:placeId/*
     */
    @Override
    public void route() {
        // Single place endpoint
        PATH("/places/:placeId", () -> {
            GET("", this::get);
            GET("/cards", this::cards);

            // Additional sorted data
            PATH("/data", () -> {
                GET("/article", this::getArticles);
                GET("/instagram", this::getInstagramMedias);
                // Reviews
            });
        });
    }

    /**
     * GET = /places/:placeId
     *
     * @param call json call
     * @return Place or Null
     */
    private Place get(JsonCall call) {
        String placeId = call.pathString("placeId");
        return dataClient.get(placeId);
    }

    /**
     * GET = /places/:placeId/cards
     *
     * @param call json call
     * @return {cards: List of PlaceCard, place: Place}
     */
    private JsonNode cards(JsonCall call) {
        String placeId = call.pathString("placeId");
        Place place = dataClient.get(placeId);
        if (place == null) return null;

        List<PlaceCard> cards = cardReader.get(place);

        // Put into node and return
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("cards", objectMapper.valueToTree(cards));
        objectNode.set("place", objectMapper.valueToTree(place));
        return nodes(200, objectNode);
    }

    private List<Article> getArticles(JsonCall call) {
        String placeId = call.pathString("placeId");
        String maxSort = call.queryString("maxSort", null);
        return articleClient.list(placeId, null, maxSort, querySize(call));
    }

    private List<InstagramMedia> getInstagramMedias(JsonCall call) {
        String placeId = call.pathString("placeId");
        String maxSort = call.queryString("maxSort", null);
        return instagramMediaClient.listByPlace(placeId, null, maxSort, querySize(call));
    }

    private static int querySize(JsonCall call) {
        int size = call.queryInt("size", 20);
        if (size <= 0) return 20;
        if (size >= 20) return 20;
        return size;
    }
}
