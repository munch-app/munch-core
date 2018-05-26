package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.places.PlaceCardReader;
import munch.api.services.places.loader.PlaceArticleCardLoader;
import munch.article.clients.Article;
import munch.article.clients.ArticleClient;
import munch.collections.LikedPlaceClient;
import munch.corpus.instagram.InstagramMedia;
import munch.corpus.instagram.InstagramMediaClient;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;
import munch.restful.client.dynamodb.NextNodeList;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.TokenAuthenticator;

import java.util.List;
import java.util.Optional;

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

    private final TokenAuthenticator<?> authenticator;
    private final LikedPlaceClient likedPlaceClient;

    @Inject
    public PlaceService(PlaceClient placeClient, ArticleClient articleClient, InstagramMediaClient instagramMediaClient, PlaceCardReader cardReader, TokenAuthenticator authenticator, LikedPlaceClient likedPlaceClient) {
        this.dataClient = placeClient;
        this.articleClient = articleClient;
        this.instagramMediaClient = instagramMediaClient;
        this.cardReader = cardReader;
        this.authenticator = authenticator;
        this.likedPlaceClient = likedPlaceClient;
    }

    /**
     * Endpoint: /v/places/*
     * Endpoint: /v/places/:placeId/*
     */
    @Override
    public void route() {
        // Places Endpoint
        PATH("/places/:placeId", () -> {
            GET("", this::get);
            GET("/cards", this::cards);

            // Partners Instagram & Article content
            PATH("/partners", () -> {
                GET("/articles", this::getArticles);
                GET("/instagram/medias", this::getInstagramMedias);
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
        Optional<String> userId = authenticator.optionalSubject(call);
        String placeId = call.pathString("placeId");
        Place place = dataClient.get(placeId);
        if (place == null) return null;

        List<PlaceCard> cards = cardReader.get(place);

        // Put cards & place data
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("cards", objectMapper.valueToTree(cards));
        objectNode.set("place", objectMapper.valueToTree(place));

        // Put user data if user exist
        userId.ifPresent(id -> {
            objectNode.putObject("user")
                    .put("liked", likedPlaceClient.isLiked(id, placeId));
        });

        return nodes(200, objectNode);
    }

    private JsonNode getArticles(JsonCall call) {
        int size = querySize(call);
        String placeId = call.pathString("placeId");
        String nextPlaceSort = call.queryString("next.placeSort", null);


        NextNodeList<Article> nextNodeList = articleClient.list(placeId, nextPlaceSort, size);
        PlaceArticleCardLoader.removeBadData(nextNodeList);

        ObjectNode node = nodes(200, nextNodeList);
        if (nextNodeList.hasNext()) node.set("next", nextNodeList.getNext());
        return node;
    }

    private JsonNode getInstagramMedias(JsonCall call) {
        int size = querySize(call);
        String placeId = call.pathString("placeId");
        String nextPlaceSort = call.queryString("next.placeSort", null);

        NextNodeList<InstagramMedia> nextNodeList = instagramMediaClient.listByPlace(placeId, nextPlaceSort, size);

        ObjectNode node = nodes(200, nextNodeList);
        if (nextNodeList.hasNext()) node.set("next", nextNodeList.getNext());
        return node;
    }

    private static int querySize(JsonCall call) {
        int size = call.queryInt("size", 20);
        if (size <= 0) return 20;
        if (size >= 20) return 20;
        return size;
    }
}
