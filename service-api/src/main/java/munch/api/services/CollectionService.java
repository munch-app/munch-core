package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import munch.collections.*;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.restful.server.JsonCall;
import munch.restful.server.auth0.authenticate.JwtAuthenticator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 16/1/2018
 * Time: 8:32 AM
 * Project: munch-core
 */
@Singleton
public final class CollectionService extends AbstractService {
    private final JwtAuthenticator authenticator;
    private final LikedPlaceClient likedPlaceClient;

    private final CollectionClient collectionClient;
    private final CollectionPlaceClient collectionPlaceClient;

    private final PlaceClient placeClient;

    @Inject
    public CollectionService(JwtAuthenticator authenticator, CollectionClient collectionClient, LikedPlaceClient likedPlaceClient, CollectionPlaceClient collectionPlaceClient, PlaceClient placeClient) {
        this.authenticator = authenticator;
        this.collectionClient = collectionClient;
        this.likedPlaceClient = likedPlaceClient;
        this.collectionPlaceClient = collectionPlaceClient;
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        BEFORE("/collections/*", authenticator::authenticate);

        PATH("/collections", () -> {
            PATH("/likes", () -> {
                GET("/list", this::listLiked);
                PUT("/:placeId", this::putLike);
                DELETE("/:placeId", this::removeLike);
            });

            GET("/list", this::listCollection);
            POST("/new", this::postCollection);

            PATH("/:collectionId", () -> {
                GET("", this::getCollection);
                PUT("", this::putCollection);
                DELETE("", this::deleteCollection);

                PATH("/:placeId", () -> {
                    PUT("", this::addPlace);
                    DELETE("", this::removePlace);
                });
            });
        });
    }

    private JsonNode listLiked(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String maxSortKey = call.queryString("maxSortKey", null);
        int size = call.queryInt("size", 20);

        ArrayNode likes = objectMapper.createArrayNode();
        for (LikedPlace likedPlace : likedPlaceClient.list(subject, maxSortKey, size)) {
            likes.addObject()
                    .put("sortKey", likedPlace.getSortKey())
                    .set("place", objectMapper.valueToTree(placeClient.get(likedPlace.getPlaceId())));
        }
        return nodes(200, likes);
    }

    private JsonNode putLike(JsonCall call) {
        String placeId = call.pathString("placeId");
        String subject = call.getJWT().getSubject();
        likedPlaceClient.add(subject, placeId);
        return Meta200;
    }

    private JsonNode removeLike(JsonCall call) {
        String placeId = call.pathString("placeId");
        String subject = call.getJWT().getSubject();
        likedPlaceClient.remove(subject, placeId);
        return Meta200;
    }

    private List<PlaceCollection> listCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String maxSortKey = call.queryString("maxSortKey", null);
        int size = call.queryInt("size", 20);

        return collectionClient.list(subject, maxSortKey, size);
    }

    private PlaceCollection postCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();

        PlaceCollection placeCollection = call.bodyAsObject(PlaceCollection.class);
        placeCollection.setUserId(subject);

        collectionClient.put(placeCollection);
        return placeCollection;
    }

    private PlaceCollection getCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");
        return collectionClient.get(subject, collectionId);
    }

    private JsonNode listPlaceCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");

        String maxSortKey = call.queryString("maxSortKey", null);
        int size = call.queryInt("size", 10);

        ArrayNode arrayNode = objectMapper.createArrayNode();
        collectionPlaceClient.list(subject, collectionId, maxSortKey, size).forEach(addedPlace -> {
            arrayNode.addObject()
                    .put("createdDate", addedPlace.getCreatedDate().getTime())
                    .put("placeId", addedPlace.getPlaceId())
                    .set("place", objectMapper.valueToTree(placeClient.get(addedPlace.getPlaceId())));
        });
        return nodes(200, arrayNode);
    }

    private PlaceCollection putCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");

        PlaceCollection placeCollection = call.bodyAsObject(PlaceCollection.class);
        placeCollection.setUserId(subject);
        placeCollection.setCollectionId(collectionId);

        collectionClient.put(placeCollection);
        return placeCollection;
    }

    private JsonNode deleteCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");

        collectionClient.delete(subject, collectionId);
        return Meta200;
    }

    private JsonNode addPlace(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        PlaceCollection collection = collectionClient.get(subject, collectionId);
        if (collection == null) return Meta404;

        Place place = placeClient.get(placeId);
        if (place == null) return Meta404;

        // Put Thumbnail if need to
        if (collection.getThumbnail() == null) {
            collection.setThumbnail(place.getImages().get(0).getImages());
            collectionClient.put(collection);
        }

        collectionPlaceClient.add(subject, collectionId, placeId);
        return Meta200;
    }

    private JsonNode removePlace(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        collectionPlaceClient.remove(subject, collectionId, placeId);
        return Meta200;
    }
}
