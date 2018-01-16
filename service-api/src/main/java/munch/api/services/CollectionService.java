package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import munch.collections.CollectionClient;
import munch.collections.LikedPlace;
import munch.collections.LikedPlaceClient;
import munch.collections.PlaceCollection;
import munch.data.clients.PlaceClient;
import munch.restful.core.RestfulMeta;
import munch.restful.server.JsonCall;
import munch.restful.server.auth0.authenticate.JwtAuthenticator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
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
    private final CollectionClient collectionClient;
    private final LikedPlaceClient likedPlaceClient;

    private final PlaceClient placeClient;

    @Inject
    public CollectionService(JwtAuthenticator authenticator, CollectionClient collectionClient, LikedPlaceClient likedPlaceClient, PlaceClient placeClient) {
        this.authenticator = authenticator;
        this.collectionClient = collectionClient;
        this.likedPlaceClient = likedPlaceClient;
        this.placeClient = placeClient;
    }

    @Override
    public void route() {
        BEFORE("/collections/*", authenticator::authenticate);

        PATH("/collections", () -> {
            PATH("/likes", () -> {
                GET("/list", this::listLiked);
                PUT("/:placeId", this::like);
                DELETE("/:placeId", this::unlike);
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

    private JsonNode like(JsonCall call) {
        String placeId = call.pathString("placeId");
        String subject = call.getJWT().getSubject();
        likedPlaceClient.add(subject, placeId);
        return Meta200;
    }

    private JsonNode unlike(JsonCall call) {
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

    private JsonNode getCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");
        PlaceCollection collection = collectionClient.get(subject, collectionId);
        // TODO Query Style
        return null;
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
        collection.getAddedPlaces().removeIf(addedPlace -> addedPlace.getPlaceId().equalsIgnoreCase(placeId));

        if (collection.getAddedPlaces().size() >= 100) {
            return nodes(RestfulMeta.builder()
                    .code(400)
                    .errorType("LimitException")
                    .errorMessage("You can only have 100 places in your collection")
                    .build());
        }

        PlaceCollection.AddedPlace addedPlace = new PlaceCollection.AddedPlace();
        addedPlace.setPlaceId(placeId);
        addedPlace.setCreatedDate(new Date());
        collection.getAddedPlaces().add(addedPlace);

        collectionClient.put(collection);
        return Meta200;
    }

    private JsonNode removePlace(JsonCall call) {
        String subject = call.getJWT().getSubject();
        String collectionId = call.pathString("collectionId");
        String placeId = call.pathString("placeId");

        PlaceCollection collection = collectionClient.get(subject, collectionId);
        if (collection == null) return Meta404;
        collection.getAddedPlaces()
                .removeIf(addedPlace -> addedPlace.getPlaceId().equalsIgnoreCase(placeId));
        collectionClient.put(collection);
        return Meta200;
    }
}
