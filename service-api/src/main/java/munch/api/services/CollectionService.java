package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.collections.*;
import munch.data.clients.PlaceClient;
import munch.data.structure.Place;
import munch.restful.core.RestfulMeta;
import munch.restful.core.exception.ParamException;
import munch.restful.server.JsonCall;
import munch.restful.server.jwt.TokenAuthenticator;

import javax.annotation.Nullable;
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
    private final TokenAuthenticator authenticator;

    private final PlaceClient placeClient;

    private final LikedPlaceClient likedPlaceClient;
    private final RecentPlaceClient recentPlaceClient;
    private final CollectionClient collectionClient;
    private final CollectionPlaceClient collectionPlaceClient;

    @Inject
    public CollectionService(TokenAuthenticator authenticator, CollectionClient collectionClient, LikedPlaceClient likedPlaceClient, CollectionPlaceClient collectionPlaceClient, PlaceClient placeClient, RecentPlaceClient recentPlaceClient) {
        this.authenticator = authenticator;
        this.collectionClient = collectionClient;
        this.likedPlaceClient = likedPlaceClient;
        this.collectionPlaceClient = collectionPlaceClient;
        this.placeClient = placeClient;
        this.recentPlaceClient = recentPlaceClient;
    }

    @Override
    public void route() {
        BEFORE("/collections", authenticator::authenticate);
        BEFORE("/collections/*", authenticator::authenticate);

        PATH("/collections", () -> {
            PATH("/likes", () -> {
                Liked liked = new Liked();
                GET("", liked::list);
                PUT("/:placeId", liked::put);
                DELETE("/:placeId", liked::delete);
            });

            PATH("/recents", () -> {
                Recent recent = new Recent();
                PUT("/:placeId", recent::put);
            });

            GET("", this::listCollection);
            POST("", this::createCollection);

            PATH("/:collectionId", () -> {
                CollectionId collectionId = new CollectionId();
                GET("", collectionId::get);
                PUT("", collectionId::put);
                DELETE("", collectionId::delete);

                PATH("/places", () -> {
                    GET("", collectionId::listPlace);
                    PUT("/:placeId", collectionId::addPlace);
                    DELETE("/:placeId", collectionId::removePlace);
                });
            });
        });
    }

    private class Liked {
        private JsonNode list(JsonCall call) {
            String subject = call.getJWT().getSubject();
            Long maxSortKey = queryLong(call, "maxSortKey");
            int size = call.queryInt("size", 20);

            List<LikedPlace> likedPlaces = likedPlaceClient.list(subject, maxSortKey, size);
            List<JsonNode> results = placeClient.batchGetAs(likedPlaces, LikedPlace::getPlaceId, (likedPlace, place) -> {
                return place.map(place1 -> objectMapper.createObjectNode()
                        .put("sortKey", likedPlace.getSortKey())
                        .put("createdDate", likedPlace.getCreatedDate())
                        .set("place", objectMapper.valueToTree(place1)));
            });

            ObjectNode nodes = nodes(200, results);
            // Add nextMaxSortKey if there is more to get
            if (likedPlaces.size() == size) {
                LikedPlace last = likedPlaces.get(size - 1);
                nodes.put("nextMaxSortKey", last.getPlaceId());
            }
            return nodes;
        }

        private JsonNode put(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String placeId = call.pathString("placeId");

            likedPlaceClient.add(subject, placeId);
            return Meta200;
        }

        private JsonNode delete(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String placeId = call.pathString("placeId");

            likedPlaceClient.remove(subject, placeId);
            return Meta200;
        }
    }

    private class Recent {
        private JsonNode put(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String placeId = call.pathString("placeId");

            recentPlaceClient.add(subject, placeId);
            return Meta200;
        }
    }

    private class CollectionId {
        private PlaceCollection get(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String collectionId = call.pathString("collectionId");
            return collectionClient.get(subject, collectionId);
        }

        private JsonNode put(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String collectionId = call.pathString("collectionId");

            PlaceCollection placeCollection = call.bodyAsObject(PlaceCollection.class);
            placeCollection.setUserId(subject);
            placeCollection.setCollectionId(collectionId);

            collectionClient.put(placeCollection);
            return nodes(200, placeCollection);
        }

        private JsonNode delete(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String collectionId = call.pathString("collectionId");

            collectionClient.delete(subject, collectionId);
            return Meta200;
        }

        private JsonNode listPlace(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String collectionId = call.pathString("collectionId");
            Long maxSortKey = queryLong(call, "maxSortKey");
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

        private JsonNode addPlace(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String collectionId = call.pathString("collectionId");
            String placeId = call.pathString("placeId");

            PlaceCollection collection = collectionClient.get(subject, collectionId);
            if (collection == null) return Meta404;

            Place place = placeClient.get(placeId);
            if (place == null) return Meta404;

            if (collection.getCount() >= 100) {
                return nodes(RestfulMeta.builder()
                        .errorType("LimitException")
                        .errorMessage("You can only have 100 places in a collection.")
                        .build());
            }

            // Put Thumbnail if need to
            if (collection.getThumbnail() == null) {
                collection.setThumbnail(place.getImages().get(0).getImages());
            }

            // Get Count After Put
            collectionPlaceClient.add(subject, collectionId, placeId);
            collection.setCount(collectionPlaceClient.count(subject, collectionId));
            collectionClient.put(collection);
            return Meta200;
        }

        private JsonNode removePlace(JsonCall call) {
            String subject = call.getJWT().getSubject();
            String collectionId = call.pathString("collectionId");
            String placeId = call.pathString("placeId");

            PlaceCollection collection = collectionClient.get(subject, collectionId);
            if (collection == null) return Meta404;

            collectionPlaceClient.remove(subject, collectionId, placeId);

            // Update Count after update, (might need consistent read)
            collection.setCount(collectionPlaceClient.count(subject, collectionId));
            collectionClient.put(collection);
            return Meta200;
        }
    }

    private List<PlaceCollection> listCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();
        Long maxSortKey = queryLong(call, "maxSortKey");
        int size = call.queryInt("size", 20);

        return collectionClient.list(subject, maxSortKey, size);
    }

    private PlaceCollection createCollection(JsonCall call) {
        String subject = call.getJWT().getSubject();

        PlaceCollection placeCollection = call.bodyAsObject(PlaceCollection.class);
        placeCollection.setUserId(subject);

        collectionClient.put(placeCollection);
        return placeCollection;
    }

    @Nullable
    private static Long queryLong(JsonCall call, String name) {
        String value = call.queryString(name, null);
        if (value == null) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ParamException(name + " must be a number.");
        }
    }
}
