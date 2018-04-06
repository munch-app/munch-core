package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.places.PlaceCardReader;
import munch.api.services.places.partner.PartnerContentManager;
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
    private final PartnerContentManager partnerContentManager;
    private final PlaceCardReader cardReader;

    private final TokenAuthenticator authenticator;
    private final LikedPlaceClient likedPlaceClient;

    @Inject
    public PlaceService(PlaceClient placeClient, ArticleClient articleClient, InstagramMediaClient instagramMediaClient, PartnerContentManager partnerContentManager, PlaceCardReader cardReader, TokenAuthenticator authenticator, LikedPlaceClient likedPlaceClient) {
        this.dataClient = placeClient;
        this.articleClient = articleClient;
        this.instagramMediaClient = instagramMediaClient;
        this.partnerContentManager = partnerContentManager;
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

            // Additional sorted data
            PATH("/data", () -> {
                GET("/article", this::getArticles);
                GET("/instagram", this::getInstagramMedias);
            });

            GET("/partners/content", this::getPartnerContent);
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

    private PartnerContentManager.PartnerContentResult getPartnerContent(JsonCall call) {
        String placeId = call.pathString("placeId");
        String articleMaxSort = call.queryString("articleMaxSort", null);
        String mediaMaxSort = call.queryString("mediaMaxSort", null);

        return partnerContentManager.query(placeId, articleMaxSort, mediaMaxSort);
    }

    private static int querySize(JsonCall call) {
        int size = call.queryInt("size", 20);
        if (size <= 0) return 20;
        if (size >= 20) return 20;
        return size;
    }
}
