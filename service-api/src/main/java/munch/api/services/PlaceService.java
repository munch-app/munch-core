package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.places.PlaceCardReader;
import munch.article.clients.Article;
import munch.article.clients.ArticleClient;
import munch.collections.LikedPlaceClient;
import munch.corpus.instagram.InstagramMedia;
import munch.corpus.instagram.InstagramMediaClient;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.AuthenticatedToken;
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

    private final TokenAuthenticator authenticator;
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
                GET("/article", this::getArticles);
                GET("/instagram", this::getInstagramMedias);
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
        Optional<AuthenticatedToken> optionalJwt = authenticator.optional(call);
        String placeId = call.pathString("placeId");
        Place place = dataClient.get(placeId);
        if (place == null) return null;

        List<PlaceCard> cards = cardReader.get(place);

        // Put cards & place data
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("cards", objectMapper.valueToTree(cards));
        objectNode.set("place", objectMapper.valueToTree(place));

        // Put user data if user exist
        optionalJwt.ifPresent(jwt -> {
            objectNode.putObject("user")
                    .put("liked", likedPlaceClient.isLiked(jwt.getSubject(), placeId));
        });

        return nodes(200, objectNode);
    }

    private JsonNode getArticles(JsonCall call) {
        int size = querySize(call);
        String placeId = call.pathString("placeId");
        String maxSort = call.queryString("maxSort", null);

        List<Article> articles = articleClient.list(placeId, null, maxSort, size);

        return nodes(200, articles)
                .put("nextMaxSort", size == articles.size() ? articles.get(size - 1).getPlaceSort() : null);
    }

    private JsonNode getInstagramMedias(JsonCall call) {
        int size = querySize(call);
        String placeId = call.pathString("placeId");
        String maxSort = call.queryString("maxSort", null);

        List<InstagramMedia> mediaList = instagramMediaClient.listByPlace(placeId, null, maxSort, size);

        return nodes(200, mediaList)
                .put("nextMaxSort", size == mediaList.size() ? mediaList.get(size - 1).getPlaceSort() : null);
    }

    private static int querySize(JsonCall call) {
        int size = call.queryInt("size", 20);
        if (size <= 0) return 20;
        if (size >= 20) return 20;
        return size;
    }
}
