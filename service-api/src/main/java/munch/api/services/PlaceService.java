package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.api.services.places.PlaceCardReader;
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
    private final PlaceCardReader cardReader;

    @Inject
    public PlaceService(PlaceClient placeClient, PlaceCardReader cardReader) {
        this.dataClient = placeClient;
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
                // Instagram
                // Article
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
     * @return List of Cards in Order
     */
    private List<PlaceCard> cards(JsonCall call) {
        String placeId = call.pathString("placeId");
        return cardReader.get(placeId);
    }
}
